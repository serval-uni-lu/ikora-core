package org.ikora.builder;

import org.ikora.error.ErrorMessages;
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

    private Linker(Runtime runtime){
        this.runtime = runtime;
    }

    private static final Pattern gherkinPattern;

    static {
        gherkinPattern =  Pattern.compile("^(\\s*)(Given|When|Then|And|But)", Pattern.CASE_INSENSITIVE);
    }

    public static void link(Runtime runtime) {
        Linker linker = new Linker(runtime);

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

    public static void link(KeywordCall call, Runtime runtime) {
        Linker linker = new Linker(runtime);

        Matcher matcher = gherkinPattern.matcher(call.getName());
        String name = matcher.replaceAll("").trim();

        linker.resolveCall(call, name);
    }

    private List<ScopeValue> linkSteps(TestCase testCase) {
        List<ScopeValue> unresolvedArguments = new ArrayList<>();

        KeywordCall setup = testCase.getSetup();
        if(setup != null){
            unresolvedArguments.addAll(resolveCall(setup, setup.getName()));
        }

        for(Step step: testCase) {
            if(!(step instanceof KeywordCall)) {
                runtime.getErrors().registerSyntaxError(
                        step.getFile(),
                        ErrorMessages.EXPECTING_KEYWORD_CALL,
                        step.getPosition()
                );
            }
            else {
                KeywordCall call = (KeywordCall)step;

                Matcher matcher = gherkinPattern.matcher(step.getName());
                String name = matcher.replaceAll("").trim();

                unresolvedArguments.addAll(resolveCall(call, name));
            }
        }

        KeywordCall teardown = testCase.getTearDown();
        if(teardown != null){
            unresolvedArguments.addAll(resolveCall(teardown, teardown.getName()));
        }

        return unresolvedArguments;
    }

    private List<ScopeValue> linkSteps(UserKeyword userKeyword) throws RuntimeException {
        List<ScopeValue> unresolvedArguments = new ArrayList<>();

        for (Step step: userKeyword) {
            step.getKeywordCall().ifPresent(call -> unresolvedArguments.addAll(resolveCall(call, call.getName())));
        }

        return unresolvedArguments;
    }

    private List<ScopeValue> resolveCall(KeywordCall call, String name) {
        List<ScopeValue> unresolvedArguments = new ArrayList<>();

        Set<? super Keyword> keywords = getKeywords(name, call.getSourceFile());

        for(Object keyword: keywords) {
            try {
                    call.linkKeyword((Keyword) keyword, Link.Import.STATIC);
                } catch (InvalidImportTypeException e) {
                    runtime.getErrors().registerInternalError(
                            call.getFile(),
                            "Should handle Static import at this point",
                            call.getPosition()
                    );
                } catch (InvalidDependencyException e) {
                    runtime.getErrors().registerSymbolError(
                            ((Keyword) keyword).getFile(),
                            e.getMessage(),
                            ((Keyword) keyword).getPosition()
                    );
                }
        }

        if(keywords.isEmpty()){
            try{
                unresolvedArguments.add(new ScopeValue(
                        (KeywordDefinition) call.getParent(),
                        new Value(name),
                        call.getPosition())
                );
            } catch (InvalidDependencyException e) {
                runtime.getErrors().registerSymbolError(
                        call.getFile(),
                        e.getMessage(),
                        call.getPosition()
                );
            }
        }


        unresolvedArguments.addAll(linkCallArguments(call));

        return unresolvedArguments;
    }

    private List<ScopeValue> linkCallArguments(KeywordCall call) {
        List<ScopeValue> unresolvedArguments = new ArrayList<>();
        List<Argument> oldArgumentList = new ArrayList<>(call.getArgumentList());

        Iterator<Argument> iterator = oldArgumentList.iterator();

        call.clearArguments();
        while(iterator.hasNext()){
            Argument argument = iterator.next();
            Set<? super Keyword> keywords = getKeywords(argument.getName(), argument.getSourceFile());

            if(keywords.isEmpty()){
                call.addArgument(argument);
            }
            else if(keywords.size() == 1){
                try {
                    Keyword keyword = (Keyword)keywords.iterator().next();
                    KeywordCall keywordCall = createKeywordCall(keyword, argument, iterator);
                    Argument keywordArgument = new Argument(call, keywordCall.toString());

                    keywordArgument.setPosition(keywordCall.getPosition());
                    keywordArgument.setCall(keywordCall);

                    call.addArgument(keywordArgument);
                } catch (InvalidDependencyException e) {
                    runtime.getErrors().registerSymbolError(
                            call.getFile(),
                            e.getMessage(),
                            argument.getPosition()
                    );

                    call.clearArguments();
                    oldArgumentList.forEach(call::addArgument);

                    break;
                }
            }
            else{
                runtime.getErrors().registerSymbolError(
                        call.getFile(),
                        "Found more than one keyword to match argument",
                        argument.getPosition()
                );

                iterator.forEachRemaining(call::addArgument);
            }
        }

        updateScope(call);

        return unresolvedArguments;
    }

    private KeywordCall createKeywordCall(Keyword keyword, Argument first, Iterator<Argument> iterator) throws InvalidDependencyException {
        KeywordCall call = new KeywordCall(first.getName());

        call.setSourceFile(first.getSourceFile());
        call.addDependency(keyword);

        Argument last = first;

        int i = keyword.getMaxNumberArguments();

        while (iterator.hasNext() && i > 0){
            last = iterator.next();

            Argument current = new Argument(call, last.getName());
            current.setPosition(last.getPosition());

            call.addArgument(current);
            --i;
        }

        call.setPosition(first.getPosition().merge(last.getPosition()));

        resolveCall(call, call.getName());

        return call;
    }

    private void updateScope(KeywordCall call) {
        for(Keyword keyword: call.getAllPotentialKeywords(Link.Import.BOTH)){
            if(keyword instanceof ScopeModifier){
                ((ScopeModifier)keyword).addToScope(runtime, call);
            }
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
                runtime.getErrors().registerUnhandledError(
                        sourceFile.getFile(),
                        String.format("Failed to load library keyword: %s::%s", library, name),
                        exception
                );
            }
        }

        return keywordsFound;
    }

    private void processUnresolvedArguments(List<ScopeValue> unresolvedArguments) {
        for(ScopeValue valueScope: unresolvedArguments){
            Set<Variable> variables = runtime.findInScope(valueScope.getTestCases(), valueScope.getSuites(), valueScope.getName());

            if(!variables.isEmpty()){
                for(Variable variable: variables){
                    try {
                        valueScope.getValue().setVariable(valueScope.getName(), variable);
                    } catch (InvalidDependencyException e) {
                        runtime.getErrors().registerInternalError(
                                valueScope.getFile(),
                                e.getMessage(),
                                valueScope.getPosition()
                        );
                    }
                }
            }
            else {
                runtime.getErrors().registerSymbolError(
                        valueScope.getFile(),
                        String.format("Found no definition for '%s'", valueScope.getName()),
                        valueScope.getPosition()
                );
            }
        }
    }
}