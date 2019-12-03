package org.ukwikora.builder;

import org.ukwikora.error.Error;
import org.ukwikora.error.InternalError;
import org.ukwikora.error.SyntaxError;
import org.ukwikora.exception.InvalidDependencyException;
import org.ukwikora.exception.InvalidImportTypeException;
import org.ukwikora.exception.MissingKeywordException;
import org.ukwikora.model.*;
import org.ukwikora.runner.Runtime;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Linker {
    private final Runtime runtime;
    private final List<Error> errors;

    private Linker(Runtime runtime, List<Error> errors){
        this.runtime = runtime;
        this.errors = errors;
    }

    private static final Pattern gherkinPattern;

    static {
        gherkinPattern =  Pattern.compile("^(\\s*)(Given|When|Then|And|But)", Pattern.CASE_INSENSITIVE);
    }

    public static void link(Runtime runtime, List<Error> errors) {
        Linker linker = new Linker(runtime, errors);

        List<ScopeValue> unresolvedArguments = new ArrayList<>();

        for (SourceFile sourceFile : runtime.getSourceFiles()) {
            for(TestCase testCase: sourceFile.getTestCases()) {
                unresolvedArguments.addAll(linker.linkSteps(testCase));
            }

            for(UserKeyword userKeyword: sourceFile.getUserKeywords()) {
                unresolvedArguments.addAll(linker.linkSteps(userKeyword));
            }
        }

        linker.processUnresolvedArguments(unresolvedArguments);
    }

    public static void link(KeywordCall call, Runtime runtime, List<Error> errors) {
        Linker linker = new Linker(runtime, errors);

        Matcher matcher = gherkinPattern.matcher(call.getName());
        String name = matcher.replaceAll("").trim();

        linker.resolveCall(runtime.getTestCase(), call, name);
    }

    private List<ScopeValue> linkSteps(TestCase testCase) {
        List<ScopeValue> unresolvedArguments = new ArrayList<>();

        KeywordCall setup = testCase.getSetup();
        if(setup != null){
            unresolvedArguments.addAll(resolveCall(testCase, setup, setup.getName().trim()));
        }

        for(Step step: testCase) {
            if(!(step instanceof KeywordCall)) {
                SyntaxError error = new SyntaxError("Expecting a step of type keyword call",
                        step.getFile().getFile(),
                        step.getLineRange());

                errors.add(error);
            }
            else {
                KeywordCall call = (KeywordCall)step;

                Matcher matcher = gherkinPattern.matcher(step.getName());
                String name = matcher.replaceAll("").trim();

                unresolvedArguments.addAll(resolveCall(testCase, call, name));
            }
        }

        KeywordCall teardown = testCase.getTearDown();
        if(teardown != null){
            unresolvedArguments.addAll(resolveCall(testCase, teardown, teardown.getName().trim()));
        }

        return unresolvedArguments;
    }

    private List<ScopeValue> linkSteps(UserKeyword userKeyword) throws RuntimeException {
        List<ScopeValue> unresolvedArguments = new ArrayList<>();

        for (Step step: userKeyword) {
            step.getKeywordCall().ifPresent(call -> {
                try {
                    String name = call.getName().trim();
                    unresolvedArguments.addAll(resolveCall(userKeyword, call, name));
                } catch (Exception e) {
                    throw new RuntimeException(e.getMessage());
                }
            });
        }

        return unresolvedArguments;
    }

    private List<ScopeValue> resolveCall(KeywordDefinition parentKeyword, KeywordCall call, String name) {
        for(Object keyword: getKeywords(name, parentKeyword.getFile())){
            try {
                call.linkKeyword((Keyword)keyword, Link.Import.STATIC);
            }
            catch (InvalidImportTypeException e){
                InternalError error = new InternalError("Should handle Static import at this point but didn't!", e);
                errors.add(error);
            } catch (InvalidDependencyException e) {
                e.printStackTrace();
            }

        }

        return linkCall(parentKeyword, call);
    }

    private List<ScopeValue> linkCall(KeywordDefinition parentKeyword, KeywordCall call) throws Exception {
        List<ScopeValue> unresolvedArguments = new ArrayList<>();

        for(Value value : call.getParameters()) {
            unresolvedArguments.addAll(resolveArgument(value, parentKeyword.getFile(), parentKeyword));
        }

        linkStepArguments(call);
        updateScope(call);

        return unresolvedArguments;
    }

    private void updateScope(KeywordCall call) throws Exception {
        for(Keyword keyword: call.getAllPotentialKeywords(Link.Import.BOTH)){
            if(keyword instanceof ScopeModifier){
                ((ScopeModifier)keyword).addToScope(runtime, call);
            }
        }
    }

    private void linkStepArguments(KeywordCall step) {
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
                Set<? super Keyword> keywords = getKeywords(keywordParameter, step.getParent().getFile());

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

                            linkStepArguments(call);
                        }
                );

            } catch (Exception e) {
                logger.error(e.getMessage());
            }
        }
    }

    private Set<? super Keyword> getKeywords(String fullName, SourceFile sourceFile) {
        Set<? super Keyword> keywordsFound = getKeywords(fullName, sourceFile, false);

        if(keywordsFound.isEmpty()){
            keywordsFound = getKeywords(fullName, sourceFile, true);
        }

        if(keywordsFound.isEmpty()) {
            logger.error("Keyword definition for \"" + fullName + "\" in \"" + sourceFile.getName() + "\" not found!");
        }
        else if(keywordsFound.size() > 1){
            logger.error("Multiple definition found for \"" + fullName + "\" in \"" + sourceFile.getName() + "\"!");
        }

        return keywordsFound;
    }

    private Set<? super Keyword> getKeywords(String fullName, SourceFile sourceFile, boolean allowSplit) {
        String library;
        String name;

        if(allowSplit){
            List<String> particles = Arrays.asList(fullName.split("\\."));
            library = particles.size() > 1 ? String.join(".", particles.subList(0, particles.size() - 1)) : "";
            name = particles.get(particles.size() - 1);
        }
        else {
            library = "";
            name = fullName;
        }

        final Set<? super Keyword> keywordsFound = new HashSet<>(sourceFile.findUserKeyword(library, name));

        if(keywordsFound.isEmpty()){
            Keyword runtimeKeyword = runtime.findKeyword(library, name);
            if(runtimeKeyword != null){
                keywordsFound.add(runtimeKeyword);
            }
        }

        return keywordsFound;
    }

    private List<ScopeValue> resolveArgument(Value value, SourceFile sourceFile, KeywordDefinition keyword) {
        List<ScopeValue> unresolvedArguments = new ArrayList<>();

        List<String> variables = value.findVariables();

        for(String name: variables){
            Set<Variable> variablesFound = new HashSet<>();

            if(keyword.getClass() == UserKeyword.class){
                variablesFound.addAll(((UserKeyword)keyword).findLocalVariable(name));
            }

            variablesFound.addAll(sourceFile.findVariable(name));
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
}