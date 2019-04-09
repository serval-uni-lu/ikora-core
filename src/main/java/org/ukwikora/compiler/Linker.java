package org.ukwikora.compiler;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.ukwikora.analytics.FindSuiteVisitor;
import org.ukwikora.analytics.FindTestCaseVisitor;
import org.ukwikora.analytics.PathMemory;
import org.ukwikora.exception.MalformedTestCaseException;
import org.ukwikora.exception.MissingKeywordException;
import org.ukwikora.model.*;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class Linker {
    private static final Logger logger = LogManager.getLogger(Linker.class);

    private final StaticRuntime runtime;

    private Linker(StaticRuntime runtime) {
        this.runtime = runtime;
    }

    private static final Pattern gherkinPattern;

    static {
        gherkinPattern =  Pattern.compile("^(\\s*)(Given|When|Then|And|But)", Pattern.CASE_INSENSITIVE);
    }

    public static void link(StaticRuntime runtime) throws Exception {
        Linker linker = new Linker(runtime);
        linker.link();
    }

    private void link() throws Exception {
        List<ScopeValue> unresolvedArguments = new ArrayList<>();

        for (TestCaseFile testCaseFile: runtime.getTestCaseFiles()) {
            for(TestCase testCase: testCaseFile.getTestCases()) {
                unresolvedArguments.addAll(linkSteps(testCase, testCaseFile));
            }

            for(UserKeyword userKeyword: testCaseFile.getUserKeywords()) {
                unresolvedArguments.addAll(linkSteps(userKeyword, testCaseFile));
            }
        }

        processUnresolvedArguments(unresolvedArguments);
    }

    private List<ScopeValue> linkSteps(TestCase testCase, TestCaseFile testCaseFile) throws Exception {
        List<ScopeValue> unresolvedArguments = new ArrayList<>();

        for(Step step: testCase) {
            if(!(step instanceof KeywordCall)) {
                throw new MalformedTestCaseException("expecting a step of type keyword call");
            }

            KeywordCall call = (KeywordCall)step;

            Matcher matcher = gherkinPattern.matcher(step.getName());
            String name = matcher.replaceAll("").trim();

            unresolvedArguments.addAll(resolveCall(testCase, testCaseFile, call, name));
        }

        return unresolvedArguments;
    }

    private List<ScopeValue> linkSteps(UserKeyword userKeyword, TestCaseFile testCaseFile) throws RuntimeException {
        List<ScopeValue> unresolvedArguments = new ArrayList<>();

        for (Step step: userKeyword) {
            step.getKeywordCall().ifPresent(call -> {
                try {
                    String name = call.getName().trim();
                    unresolvedArguments.addAll(resolveCall(userKeyword, testCaseFile, call, name));
                } catch (Exception e) {
                    throw new RuntimeException(e.getMessage());
                }
            });
        }

        return unresolvedArguments;
    }

    private List<ScopeValue> resolveCall(KeywordDefinition parentKeyword, TestCaseFile testCaseFile, KeywordCall call, String name) throws Exception {
        for(Object keyword: getKeywords(name, testCaseFile)){
            call.linkKeyword((Keyword)keyword, Link.Import.STATIC);
        }

        return linkCall(parentKeyword, testCaseFile, call);
    }

    private List<ScopeValue> linkCall(KeywordDefinition parentKeyword, TestCaseFile testCaseFile, KeywordCall call) {
        List<ScopeValue> unresolvedArguments = new ArrayList<>();

        for(Value value : call.getParameters()) {
            unresolvedArguments.addAll(resolveArgument(value, testCaseFile, parentKeyword));
        }

        linkStepArguments(call, testCaseFile);
        runtime.resolveGlobal(call);

        return unresolvedArguments;
    }

    private void linkStepArguments(KeywordCall step, TestCaseFile testCaseFile) {
        if(!step.hasParameters()){
            return;
        }

        for(int position: step.getKeywordsLaunchedPosition()){
            try {
                final Optional<Value> parameter = step.getParameter(position, true);

                if(!parameter.isPresent()){
                    throw new MissingKeywordException(String.format("Failed to get keyword at position %d for step %s in file %s",
                            position,
                            step.getName(),
                            step.getFileName()));
                }

                final String keywordParameter = parameter.get().getName();
                Set<? super Keyword> keywords = getKeywords(keywordParameter, testCaseFile);

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

                            linkStepArguments(call, testCaseFile);
                        }
                );

            } catch (Exception e) {
                logger.error(e.getMessage());
            }
        }
    }

    private Set<? super Keyword> getKeywords(String fullName, TestCaseFile testCaseFile) throws Exception{
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

    private  List<ScopeValue> resolveArgument(Value value, TestCaseFile testCaseFile, KeywordDefinition keyword) {
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

    private void processUnresolvedArguments(List<ScopeValue> unresolvedArguments) {
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
}
