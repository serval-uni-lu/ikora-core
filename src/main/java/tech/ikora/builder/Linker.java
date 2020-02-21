package tech.ikora.builder;

import org.apache.commons.lang3.tuple.Pair;
import tech.ikora.error.ErrorMessages;
import tech.ikora.exception.InvalidDependencyException;
import tech.ikora.runner.Runtime;
import tech.ikora.model.*;

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

        Set<? super Keyword> keywords = getKeywords(call.getName(), call.getSourceFile());

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


        unresolvedNodes.addAll(linkCallArguments(call));

        return unresolvedNodes;
    }

    private List<UnresolvedNode> linkCallArguments(KeywordCall call) {
        List<UnresolvedNode> unresolvedNodes = new ArrayList<>();
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
                Keyword keyword = (Keyword)keywords.iterator().next();
                KeywordCall keywordCall = createKeywordCall(keyword, argument, iterator);
                Argument keywordArgument = new Argument(keywordCall);

                call.addArgument(keywordArgument);
            }
            else{
                runtime.getErrors().registerSymbolError(
                        call.getFile(),
                        ErrorMessages.FOUND_MULTIPLE_MATCHES,
                        argument.getRange()
                );

                iterator.forEachRemaining(call::addArgument);
            }
        }

        updateScope(call);

        return unresolvedNodes;
    }

    private KeywordCall createKeywordCall(Keyword keyword, Argument first, Iterator<Argument> iterator) {
        KeywordCall call = new KeywordCall(first.getName());

        call.setSourceFile(first.getSourceFile());
        call.addDependency(keyword);

        Argument last;
        int i = keyword.getMaxNumberArguments();

        while (iterator.hasNext() && i > 0){
            last = iterator.next();

            Argument current = new Argument(last.getName());
            call.addArgument(current);
            --i;
        }

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

    private void processUnresolvedNodes(List<UnresolvedNode> unresolvedNodes) {
        for(UnresolvedNode unresolvedNode : unresolvedNodes){
            Set<Node> nodes = runtime.findInScope(unresolvedNode.getTestCases(), unresolvedNode.getSuites(), unresolvedNode.getName());

            for(Node node: nodes){
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