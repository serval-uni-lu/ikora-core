package org.ikora.builder;

import org.apache.commons.lang3.tuple.Pair;
import org.ikora.error.ErrorMessages;
import org.ikora.exception.InvalidDependencyException;
import org.ikora.exception.InvalidImportTypeException;
import org.ikora.model.*;
import org.ikora.runner.Runtime;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class Linker {
    private final Runtime runtime;

    private Linker(Runtime runtime){
        this.runtime = runtime;
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
        linker.resolveCall(call);
    }

    private List<ScopeValue> linkSteps(TestCase testCase) {
        List<ScopeValue> unresolvedArguments = new ArrayList<>();

        KeywordCall setup = testCase.getSetup();
        if(setup != null){
            unresolvedArguments.addAll(resolveCall(setup));
        }

        for (Step step: testCase) {
            if(testCase.hasTemplate()){
                try {
                    step.setTemplate(testCase.getTemplate());
                } catch (InvalidDependencyException e) {
                    runtime.getErrors().registerInternalError(
                            step.getFile(),
                            ErrorMessages.FAILED_TO_LINK_TEMPLATE,
                            step.getPosition()
                    );
                }
            }

            step.getKeywordCall().ifPresent(call -> unresolvedArguments.addAll(resolveCall(call)));
        }

        KeywordCall teardown = testCase.getTearDown();
        if(teardown != null){
            unresolvedArguments.addAll(resolveCall(teardown));
        }

        return unresolvedArguments;
    }

    private List<ScopeValue> linkSteps(UserKeyword userKeyword) throws RuntimeException {
        List<ScopeValue> unresolvedArguments = new ArrayList<>();

        for (Step step: userKeyword) {
            step.getKeywordCall().ifPresent(call -> unresolvedArguments.addAll(resolveCall(call)));
        }

        return unresolvedArguments;
    }

    private List<ScopeValue> resolveCall(KeywordCall call) {
        List<ScopeValue> unresolvedArguments = new ArrayList<>();

        Set<? super Keyword> keywords = getKeywords(call.getName(), call.getSourceFile());

        for(Object keyword: keywords) {
            try {
                    call.linkKeyword((Keyword) keyword, Link.Import.STATIC);
                } catch (InvalidImportTypeException e) {
                    runtime.getErrors().registerInternalError(
                            call.getFile(),
                            ErrorMessages.SHOULD_HANDLE_STATIC_IMPORT,
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
                        call.getCaller(),
                        new Value(call.getName()),
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
                    Argument keywordArgument = new Argument(call, keywordCall.getName());

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
                        ErrorMessages.FOUND_MULTIPLE_MATCHES,
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

        resolveCall(call);

        return call;
    }

    private void updateScope(KeywordCall call) {
        for(Keyword keyword: call.getAllPotentialKeywords(Link.Import.BOTH)){
            if(keyword instanceof ScopeModifier){
                ((ScopeModifier)keyword).addToScope(runtime, call);
            }
        }
    }

    private Set<? super Keyword> getKeywords(Token fullName, SourceFile sourceFile) {
        Set<? super Keyword> keywordsFound = getKeywords(fullName, sourceFile, false);

        if(keywordsFound.isEmpty()){
            keywordsFound = getKeywords(fullName, sourceFile, true);
        }

        return keywordsFound;
    }

    private Set<? super Keyword> getKeywords(Token fullName, SourceFile sourceFile, boolean allowSplit) {
        String library;
        Token name;

        if(allowSplit){
            Pair<Token, Token> libraryAndName = fullName.splitLibrary();

            library = libraryAndName.getLeft().getText();
            name = libraryAndName.getRight();
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
                        ErrorMessages.FAILED_TO_LOAD_LIBRARY_KEYWORD,
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
                        ErrorMessages.FOUND_NO_MATCH,
                        valueScope.getPosition()
                );
            }
        }
    }
}