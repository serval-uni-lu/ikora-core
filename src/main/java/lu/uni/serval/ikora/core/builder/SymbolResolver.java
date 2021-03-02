package lu.uni.serval.ikora.core.builder;

import lu.uni.serval.ikora.core.model.*;
import org.apache.commons.lang3.tuple.Pair;
import lu.uni.serval.ikora.core.error.ErrorMessages;
import lu.uni.serval.ikora.core.runner.Runtime;
import lu.uni.serval.ikora.core.types.BaseTypeList;
import lu.uni.serval.ikora.core.types.KeywordType;
import lu.uni.serval.ikora.core.utils.ArgumentUtils;
import lu.uni.serval.ikora.core.utils.Ast;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class SymbolResolver {
    private final Runtime runtime;

    private SymbolResolver(Runtime runtime){
        this.runtime = runtime;
    }

    public static void resolve(Runtime runtime) {
        SymbolResolver symbolResolver = new SymbolResolver(runtime);

        for (SourceFile sourceFile : runtime.getSourceFiles()) {
            for(TestCase testCase: sourceFile.getTestCases()) {
                symbolResolver.resolveSteps(testCase);
            }

            for(UserKeyword userKeyword: sourceFile.getUserKeywords()) {
                symbolResolver.resolveSteps(userKeyword);
            }

            for(VariableAssignment variableAssignment: sourceFile.getVariables()){
                symbolResolver.resolveVariable(variableAssignment.getVariable());
            }
        }
    }

    public static void resolve(KeywordCall call, Runtime runtime) {
        SymbolResolver symbolResolver = new SymbolResolver(runtime);
        symbolResolver.resolveCall(call);
    }

    private void resolveSteps(TestCase testCase) {
        testCase.getSetup().ifPresent(this::resolveCall);

        for (Step step: testCase) {
            testCase.getTemplate().ifPresent(step::setTemplate);
            step.getKeywordCall().ifPresent(this::resolveCall);
        }

        testCase.getTearDown().ifPresent(this::resolveCall);
    }

    private void resolveSteps(UserKeyword userKeyword) throws RuntimeException {
        for (Step step: userKeyword) {
            resolveStep(step);
        }
    }

    private void resolveStep(Step step){
        step.getKeywordCall().ifPresent(this::resolveCall);

        for (Step childStep: step.getSteps()) {
            resolveStep(childStep);
        }
    }

    private void resolveCall(KeywordCall call) {
        Set<? super Keyword> keywords = getKeywords(call.getNameToken(), call.getSourceFile());

        for(Object keyword: keywords) {
            call.linkKeyword((Keyword) keyword, Link.Import.STATIC);
        }

        if(keywords.isEmpty()){
            runtime.getErrors().registerSymbolError(
                    call.getSource(),
                    ErrorMessages.FOUND_NO_MATCH,
                    Range.fromTokens(call.getTokens(), null)
            );
        }

        resolveCallArguments(call);
    }

    private void resolveCallArguments(KeywordCall call) {
        for(Argument argument: call.getArgumentList()){
            resolveArgumentVariables(argument);
        }

        resolveArgumentKeyword(call);

        updateScope(call);
    }

    private void resolveArgumentVariables(Argument argument) {
        final SourceNode definition = argument.getDefinition();

        List<Variable> variables = new ArrayList<>();

        if(Variable.class.isAssignableFrom(definition.getClass())){
            variables.add((Variable)definition);
        }
        else if(Literal.class.isAssignableFrom(definition.getClass())){
            variables.addAll(((Literal)definition).getVariables());
        }

        variables.forEach(this::resolveVariable);
    }

    private void resolveArgumentKeyword(KeywordCall call){
        final Optional<Keyword> keyword = call.getKeyword();

        if(!keyword.isPresent()){
            return;
        }

        final NodeList<Argument> argumentList = call.getArgumentList();
        final BaseTypeList argumentTypes = keyword.get().getArgumentTypes();

        if(argumentTypes.containsType(KeywordType.class)){
            int keywordIndex = argumentTypes.findFirst(KeywordType.class);

            if(ArgumentUtils.isExpendedUntilPosition(argumentList, keywordIndex)){
                final List<Argument> argumentsToProcess = argumentList.subList(keywordIndex, argumentList.size());
                final KeywordCall callArgument = createKeywordArgument(argumentsToProcess);
                final NodeList<Argument> newArgumentList = new NodeList<>(argumentList.subList(0, keywordIndex));

                newArgumentList.add(new Argument(callArgument));
                call.setArgumentList(newArgumentList);

                resolveCall(callArgument);
            }
        }
    }

    private KeywordCall createKeywordArgument(List<Argument> arguments) {
        final Argument keywordName = arguments.get(0);
        final KeywordCall call = new KeywordCall(keywordName.getNameToken());

        final NodeList<Argument> argumentList = new NodeList<>();

        if(arguments.size() > 1){
            argumentList.addAll(arguments.subList(1, arguments.size()));
        }

        call.setArgumentList(argumentList);

        return call;
    }

    private void updateScope(KeywordCall call) {
        for(Keyword keyword: call.getAllPotentialKeywords(Link.Import.BOTH)){
            if(keyword instanceof ScopeModifier){
                ((ScopeModifier)keyword).addToScope(runtime, call);
            }
        }
    }

    private void resolveVariable(Variable variable){
        final Set<Dependable> definitions = new HashSet<>();

        Optional<ScopeNode> parentScope = Ast.getParentByType(variable, ScopeNode.class);
        while(parentScope.isPresent()){
            definitions.addAll(parentScope.get().findDefinition(variable));
            if(!definitions.isEmpty()) break;

            parentScope = Ast.getParentByType(parentScope.get(), ScopeNode.class);
        }

        if(definitions.isEmpty()){
            final SourceFile sourceFile = variable.getSourceFile();
            definitions.addAll(sourceFile.findVariable(variable.getNameToken()));
        }

        if(definitions.isEmpty()){
            runtime.findLibraryVariable("", variable.getNameToken());
        }

        definitions.forEach(d -> variable.linkToDefinition(d, Link.Import.STATIC));
    }

    private Set<? super Keyword> getKeywords(Token fullName, SourceFile sourceFile) {
        Set<? super Keyword> keywordsFound = getKeywords(fullName, sourceFile, false);

        if(keywordsFound.isEmpty()){
            keywordsFound = getKeywords(fullName, sourceFile, true);
        }

        return keywordsFound;
    }

    private Set<? super Keyword> getKeywords(Token fullName, SourceFile sourceFile, boolean allowSplit) {
        String library = "";
        Token name = fullName;

        if(allowSplit){
            Pair<Token, Token> libraryAndName = fullName.splitLibrary();

            library = libraryAndName.getLeft().getText();
            name = libraryAndName.getRight();
        }

        final Set<? super Keyword> keywordsFound = new HashSet<>(sourceFile.findUserKeyword(library, name));

        if(keywordsFound.isEmpty()){
            try {
                Keyword runtimeKeyword = runtime.findLibraryKeyword(library, name);

                if(runtimeKeyword != null){
                    keywordsFound.add(runtimeKeyword);
                }
            } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException exception) {
                runtime.getErrors().registerUnhandledError(
                        sourceFile.getSource(),
                        ErrorMessages.FAILED_TO_LOAD_LIBRARY_KEYWORD,
                        exception
                );
            }
        }

        return keywordsFound;
    }
}