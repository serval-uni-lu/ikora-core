package tech.ikora.builder;

import org.apache.commons.lang3.tuple.Pair;
import tech.ikora.error.ErrorMessages;
import tech.ikora.exception.InvalidDependencyException;
import tech.ikora.runner.Runtime;
import tech.ikora.model.*;
import tech.ikora.types.BaseTypeList;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class Linker {
    private final Runtime runtime;

    private Linker(Runtime runtime){
        this.runtime = runtime;
    }

    public static void link(Runtime runtime) {
        Linker linker = new Linker(runtime);

        List<UnresolvedNode> unresolvedNodes = new ArrayList<>();

        for (SourceFile sourceFile : runtime.getSourceFiles()) {
            for(TestCase testCase: sourceFile.getTestCases()) {
                unresolvedNodes.addAll(linker.linkSteps(testCase));
            }

            for(UserKeyword userKeyword: sourceFile.getUserKeywords()) {
                unresolvedNodes.addAll(linker.linkSteps(userKeyword));
            }
        }

        linker.processUnresolvedNodes(unresolvedNodes);
    }

    public static void link(KeywordCall call, Runtime runtime) {
        Linker linker = new Linker(runtime);
        linker.resolveCall(call);
    }

    private List<UnresolvedNode> linkSteps(TestCase testCase) {
        List<UnresolvedNode> unresolvedNodes = new ArrayList<>();

        KeywordCall setup = testCase.getSetup();
        if(setup != null){
            unresolvedNodes.addAll(resolveCall(setup));
        }

        for (Step step: testCase) {
            if(testCase.hasTemplate()){
                step.setTemplate(testCase.getTemplate());
            }

            step.getKeywordCall().ifPresent(call -> unresolvedNodes.addAll(resolveCall(call)));
        }

        KeywordCall teardown = testCase.getTearDown();
        if(teardown != null){
            unresolvedNodes.addAll(resolveCall(teardown));
        }

        return unresolvedNodes;
    }

    private List<UnresolvedNode> linkSteps(UserKeyword userKeyword) throws RuntimeException {
        List<UnresolvedNode> unresolvedNodes = new ArrayList<>();

        for (Step step: userKeyword) {
            step.getKeywordCall().ifPresent(call -> unresolvedNodes.addAll(resolveCall(call)));
        }

        return unresolvedNodes;
    }

    private List<UnresolvedNode> resolveCall(KeywordCall call) {
        List<UnresolvedNode> unresolvedNodes = new ArrayList<>();

        Set<? super Keyword> keywords = getKeywords(call.getNameToken(), call.getSourceFile());

        for(Object keyword: keywords) {
            call.linkKeyword((Keyword) keyword, Link.Import.STATIC);
        }

        if(keywords.isEmpty()){
            try{
                unresolvedNodes.add(new UnresolvedNode(call.getCaller(), call));
            } catch (InvalidDependencyException e) {
                runtime.getErrors().registerSymbolError(
                        call.getFile(),
                        e.getMessage(),
                        call.getRange()
                );
            }
        }


        linkCallArguments(call);

        return unresolvedNodes;
    }

    private void linkCallArguments(KeywordCall call) {
        final Optional<Keyword> keyword = call.getKeyword();

        if(!keyword.isPresent()){
            return;
        }

        final ArgumentList argumentList = call.getArgumentList();
        final BaseTypeList argumentTypes = keyword.get().getArgumentTypes();

        if(argumentTypes.containsKeyword()){
            int keywordIndex = argumentTypes.keywordIndex();

            if(isArgumentListExpendedUntilKeyword(argumentList, keywordIndex)){
                final List<Argument> argumentsToProcess = argumentList.subList(keywordIndex, argumentList.size());
                final Argument callArgument = createCallArgument(argumentsToProcess);

                final ArgumentList newArgumentList = new ArgumentList(argumentList.subList(0, keywordIndex));
                newArgumentList.add(callArgument);

                call.setArgumentList(newArgumentList);
            }
        }

        updateScope(call);
    }

    private boolean isArgumentListExpendedUntilKeyword(ArgumentList argumentList, int keywordIndex){
        int listIndex = argumentList.findFirst(ListVariable.class);
        int dictIndex = argumentList.findFirst(DictionaryVariable.class);

        int varIndex = argumentList.size();
        varIndex = listIndex != -1 ? Math.min(varIndex, listIndex) : varIndex;
        varIndex = dictIndex != -1 ? Math.min(varIndex, dictIndex) : varIndex;

        return keywordIndex < varIndex;
    }

    private Argument createCallArgument(List<Argument> arguments) {
        Argument keywordName = arguments.get(0);

        KeywordCall call = new KeywordCall(keywordName.getNameToken());
        call.setSourceFile(keywordName.getSourceFile());

        if(arguments.size() > 1){
            for(Argument argument: arguments.subList(1, arguments.size())){
                call.addArgument(argument);
            }
        }

        resolveCall(call);

        return new Argument(call);
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

    private void processUnresolvedNodes(List<UnresolvedNode> unresolvedNodes) {
        for(UnresolvedNode unresolvedNode : unresolvedNodes){
            Set<Node> nodes = runtime.findInScope(unresolvedNode.getTestCases(), unresolvedNode.getSuites(), unresolvedNode.getName());

            for(Node node : nodes){
                //TODO: Implement method to update the node that were just resolved
            }

            if(nodes.isEmpty()) {
                runtime.getErrors().registerSymbolError(
                        unresolvedNode.getFile(),
                        ErrorMessages.FOUND_NO_MATCH,
                        Range.fromToken(unresolvedNode.getName(), null)
                );

            }
        }
    }
}