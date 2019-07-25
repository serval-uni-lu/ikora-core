package org.ukwikora.builder;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.ukwikora.analytics.FindSuiteVisitor;
import org.ukwikora.analytics.FindTestCaseVisitor;
import org.ukwikora.analytics.PathMemory;
import org.ukwikora.exception.InvalidImportTypeException;
import org.ukwikora.exception.MalformedTestCaseException;
import org.ukwikora.exception.MissingKeywordException;
import org.ukwikora.model.*;
import org.ukwikora.runner.Runtime;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Linker {
    private static final Logger logger = LogManager.getLogger(Linker.class);

    private Linker() {}

    private static final Pattern gherkinPattern;

    static {
        gherkinPattern =  Pattern.compile("^(\\s*)(Given|When|Then|And|But)", Pattern.CASE_INSENSITIVE);
    }

    public static void link(Runtime runtime) throws Exception {
        List<ScopeValue> unresolvedArguments = new ArrayList<>();

        for (TestCaseFile testCaseFile: runtime.getTestCaseFiles()) {
            for(TestCase testCase: testCaseFile.getTestCases()) {
                unresolvedArguments.addAll(linkSteps(testCase, runtime));
            }

            for(UserKeyword userKeyword: testCaseFile.getUserKeywords()) {
                unresolvedArguments.addAll(linkSteps(userKeyword, runtime));
            }
        }

        processUnresolvedArguments(unresolvedArguments, runtime);
    }

    public static void link(KeywordCall call, Runtime runtime) throws Exception {
        Matcher matcher = gherkinPattern.matcher(call.getName());
        String name = matcher.replaceAll("").trim();

        resolveCall(runtime.getTestCase(), call, name, runtime);
    }

    private static List<ScopeValue> linkSteps(TestCase testCase, Runtime runtime) throws Exception {
        List<ScopeValue> unresolvedArguments = new ArrayList<>();

        KeywordCall setup = testCase.getSetup();
        if(setup != null){
            unresolvedArguments.addAll(resolveCall(testCase, setup, setup.getName().trim(), runtime));
        }

        for(Step step: testCase) {
            if(!(step instanceof KeywordCall)) {
                throw new MalformedTestCaseException("expecting a step of type keyword call");
            }

            KeywordCall call = (KeywordCall)step;

            Matcher matcher = gherkinPattern.matcher(step.getName());
            String name = matcher.replaceAll("").trim();

            unresolvedArguments.addAll(resolveCall(testCase, call, name, runtime));
        }

        KeywordCall teardown = testCase.getTearDown();
        if(teardown != null){
            unresolvedArguments.addAll(resolveCall(testCase, teardown, teardown.getName().trim(), runtime));
        }

        return unresolvedArguments;
    }

    private static List<ScopeValue> linkSteps(UserKeyword userKeyword, Runtime runtime) throws RuntimeException {
        List<ScopeValue> unresolvedArguments = new ArrayList<>();

        for (Step step: userKeyword) {
            step.getKeywordCall().ifPresent(call -> {
                try {
                    String name = call.getName().trim();
                    unresolvedArguments.addAll(resolveCall(userKeyword, call, name, runtime));
                } catch (Exception e) {
                    throw new RuntimeException(e.getMessage());
                }
            });
        }

        return unresolvedArguments;
    }

    private static List<ScopeValue> resolveCall(KeywordDefinition parentKeyword, KeywordCall call, String name, Runtime runtime) throws Exception {
        for(Object keyword: getKeywords(name, parentKeyword.getFile(), runtime)){
            try {
                call.linkKeyword((Keyword)keyword, Link.Import.STATIC);
            }
            catch (InvalidImportTypeException e){
                logger.error(e.getMessage());
            }

        }

        return linkCall(parentKeyword, call, runtime);
    }

    private static List<ScopeValue> linkCall(KeywordDefinition parentKeyword, KeywordCall call, Runtime runtime) throws Exception {
        List<ScopeValue> unresolvedArguments = new ArrayList<>();

        for(Value value : call.getParameters()) {
            unresolvedArguments.addAll(resolveArgument(value, parentKeyword.getFile(), parentKeyword, runtime));
        }

        linkStepArguments(call, runtime);
        updateScope(call, runtime);

        return unresolvedArguments;
    }

    private static void updateScope(KeywordCall call, Runtime runtime) throws Exception {
        for(Keyword keyword: call.getAllPotentialKeywords(Link.Import.BOTH)){
            if(keyword instanceof ScopeModifier){
                ((ScopeModifier)keyword).addToScope(runtime, call);
            }
        }
    }

    private static void linkStepArguments(KeywordCall step, Runtime runtime) {
        if(!step.hasParameters()){
            return;
        }

        for(int position: step.getKeywordsLaunchedPosition()){
            try {
                // try to resolve parameters
                Optional<Value> parameter = step.getParameter(position, true);

                if(!parameter.isPresent()){
                    // if not working try without resolving them
                    parameter = step.getParameter(position, false);

                    if(!parameter.isPresent()){
                        throw new MissingKeywordException(String.format("Failed to get keyword at position %d for step %s in file %s from project %s",
                                position,
                                step.getName(),
                                step.getParent().getFile().getName(),
                                step.getParent().getFile().getProject().getName()));
                    }
                }

                final String keywordParameter = parameter.get().getName();
                Set<? super Keyword> keywords = getKeywords(keywordParameter, step.getParent().getFile(), runtime);

                if(keywords.isEmpty()) {
                    throw new MissingKeywordException(String.format("Failed to find keyword parameter: %s", keywordParameter));
                }
                else if(keywords.size() > 1){
                    throw new MissingKeywordException(String.format("Found multiple definitions for : %s", keywordParameter));
                }

                Keyword keyword = (Keyword) keywords.iterator().next();

                step.getParameter(position, false).ifPresent(
                        parameterName -> {
                            KeywordCall call = step.setKeywordParameter(parameterName, keyword);

                            if(call == null){
                                throw new RuntimeException(String.format("Failed to set keyword parameter: %s", keywordParameter));
                            }

                            linkStepArguments(call, runtime);
                        }
                );

            } catch (Exception e) {
                logger.error(e.getMessage());
            }
        }
    }

    private static Set<? super Keyword> getKeywords(String fullName, TestCaseFile testCaseFile, Runtime runtime) throws Exception{
        List<String> particles = Arrays.asList(fullName.split("\\."));
        String library = particles.size() > 1 ? String.join(".", particles.subList(0, particles.size() - 1)) : "";
        String name = particles.get(particles.size() - 1);

        Set<? super Keyword> keywordsFound = new HashSet<>(testCaseFile.findUserKeyword(library, name));

        if(keywordsFound.isEmpty()){
            Keyword runtimeKeyword = runtime.findKeyword(library, name);
            if(runtimeKeyword != null){
                keywordsFound.add(runtimeKeyword);
            }
        }

        if(keywordsFound.isEmpty()) {
            logger.error("Keyword definition for \"" + name + "\" in \"" + testCaseFile.getName() + "\" not found!");
        }
        else if(keywordsFound.size() > 1){
            logger.error("Multiple definition found for \"" + name + "\" in \"" + testCaseFile.getName() + "\"!");
        }

        return keywordsFound;
    }

    private static List<ScopeValue> resolveArgument(Value value, TestCaseFile testCaseFile, KeywordDefinition keyword, Runtime runtime) {
        List<ScopeValue> unresolvedArguments = new ArrayList<>();

        List<String> variables = value.findVariables();

        for(String name: variables){
            Set<Variable> variablesFound = new HashSet<>();

            if(keyword.getClass() == UserKeyword.class){
                variablesFound.addAll(((UserKeyword)keyword).findLocalVariable(name));
            }

            variablesFound.addAll(testCaseFile.findVariable(name));
            variablesFound.addAll(runtime.findLibraryVariable(name));

            if(variablesFound.isEmpty()){
                unresolvedArguments.add(new ScopeValue(keyword, value, name));
            }
            else{
                value.setVariable(name, variablesFound);
            }
        }

        return unresolvedArguments;
    }

    private static void processUnresolvedArguments(List<ScopeValue> unresolvedArguments, Runtime runtime) {
        for(ScopeValue valueScope: unresolvedArguments){
            Set<Variable> variables = runtime.findInScope(valueScope.getTestCases(), valueScope.getSuites(), valueScope.variableName);

            if(!variables.isEmpty()){
                for(Variable variable: variables)
                valueScope.value.setVariable(valueScope.variableName, variable);
            }
            else {
                logger.error("Variable for value \"" + valueScope.variableName + "\" in \"" + valueScope.keyword.getName() + "\" not found!");
            }
        }
    }
}

class ScopeValue {
    Value value;
    String variableName;
    KeywordDefinition keyword;

    ScopeValue(KeywordDefinition keyword, Value value, String variableName){
        this.value = value;
        this.variableName = variableName;
        this.keyword = keyword;
    }

    Set<TestCase> getTestCases(){
        FindTestCaseVisitor visitor = new FindTestCaseVisitor();
        this.keyword.accept(visitor, new PathMemory());

        return visitor.getTestCases();
    }

    Set<String> getSuites(){
        FindSuiteVisitor visitor = new FindSuiteVisitor();
        this.keyword.accept(visitor, new PathMemory());

        return visitor.getSuites();
    }
}