package org.ikora.builder;

import org.ikora.error.*;
import org.ikora.exception.InvalidDependencyException;
import org.ikora.exception.InvalidImportTypeException;
import org.ikora.model.*;
import org.ikora.runner.Runtime;

import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Linker {
    private final Runtime runtime;
    private final ErrorManager errors;

    private Linker(Runtime runtime, ErrorManager errors){
        this.runtime = runtime;
        this.errors = errors;
    }

    private static final Pattern gherkinPattern;

    static {
        gherkinPattern =  Pattern.compile("^(\\s*)(Given|When|Then|And|But)", Pattern.CASE_INSENSITIVE);
    }

    public static void link(Runtime runtime, ErrorManager errors) {
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

    public static void link(KeywordCall call, Runtime runtime, ErrorManager errors) {
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
                errors.registerSyntaxError(
                        "Expecting a step of type keyword call",
                        step.getFile().getFile(),
                        step.getLineRange()
                );
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
        getKeywords(name, parentKeyword.getFile()).forEach(keyword -> {
            try {
                call.linkKeyword((Keyword) keyword, Link.Import.STATIC);
            } catch (InvalidImportTypeException e) {
                errors.registerInternalError(
                        "Should handle Static import at this point but didn't!",
                        call.getFile().getFile(),
                        call.getLineRange()
                );
            } catch (InvalidDependencyException e) {
                errors.registerSymbolError(
                        "Invalid dependency",
                        ((Keyword) keyword).getFile().getFile(),
                        ((Keyword) keyword).getLineRange()
                );
            }
        });

        return linkCall(parentKeyword, call);
    }

    private List<ScopeValue> linkCall(KeywordDefinition parentKeyword, KeywordCall call) {
        List<ScopeValue> unresolvedArguments = new ArrayList<>();

        for(Value value : call.getParameters()) {
            unresolvedArguments.addAll(resolveArgument(value, parentKeyword.getFile(), parentKeyword));
        }

        linkStepArguments(call);
        updateScope(call);

        return unresolvedArguments;
    }

    private void updateScope(KeywordCall call) {
        for(Keyword keyword: call.getAllPotentialKeywords(Link.Import.BOTH)){
            if(keyword instanceof ScopeModifier){
                ((ScopeModifier)keyword).addToScope(runtime, call, errors);
            }
        }
    }

    private void linkStepArguments(KeywordCall step) {
        if(!step.hasParameters()){
            return;
        }

        for(int position: step.getKeywordsLaunchedPosition()){
            // try to resolve parameters
            Optional<Value> parameter = step.getParameter(position, true);

            if(!parameter.isPresent()){
                // if not working try without resolving them
                parameter = step.getParameter(position, false);

                if(!parameter.isPresent()){
                    errors.registerInternalError(
                            "Failed to link keyword parameter",
                            step.getFile().getFile(),
                            step.getLineRange()
                    );

                    continue;
                }
            }

            final String keywordParameter = parameter.get().getName();
            Set<? super Keyword> keywords = getKeywords(keywordParameter, step.getParent().getFile());

            if(keywords.isEmpty()) {
                errors.registerSymbolError(
                        String.format("Found no definition for keyword parameter: %s", keywordParameter),
                        step.getFile().getFile(),
                        step.getLineRange()
                );

                continue;
            }
            else if(keywords.size() > 1){
                errors.registerSymbolError(
                        String.format("Found multiple definitions for keyword parameter: %s", keywordParameter),
                        step.getFile().getFile(),
                        step.getLineRange()
                );

                continue;
            }

            Keyword keyword = (Keyword) keywords.iterator().next();

            step.getParameter(position, false).ifPresent(
                    parameterName -> {
                        KeywordCall call = step.setKeywordParameter(parameterName, keyword);

                        if(call == null){
                            errors.registerInternalError(
                                    String.format("Failed to set keyword parameter: %s", keywordParameter),
                                    step.getFile().getFile(),
                                    step.getLineRange()
                            );
                        }
                        else{
                            linkStepArguments(call);
                        }
                    }
            );
        }
    }

    private Set<? super Keyword> getKeywords(String fullName, SourceFile sourceFile) {
        Set<? super Keyword> keywordsFound = getKeywords(fullName, sourceFile, false);

        if(keywordsFound.isEmpty()){
            keywordsFound = getKeywords(fullName, sourceFile, true);
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
            try {
                Keyword runtimeKeyword = runtime.findKeyword(library, name);

                if(runtimeKeyword != null){
                    keywordsFound.add(runtimeKeyword);
                }
            } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException exception) {
                errors.registerUnhandledError(
                    String.format("Failed to load library keyword: %s::%s", library, name),
                    exception
                );
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
                for(Variable variable: variables){
                    valueScope.value.setVariable(valueScope.variableName, variable);
                }
            }
            else {
                errors.registerSymbolError(
                    String.format("Found no definition for local variable: %s", valueScope.variableName),
                    valueScope.keyword.getFile().getFile(),
                    valueScope.keyword.getLineRange()
                );
            }
        }
    }
}