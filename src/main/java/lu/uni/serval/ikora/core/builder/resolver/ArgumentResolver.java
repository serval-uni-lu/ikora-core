package lu.uni.serval.ikora.core.builder.resolver;

import lu.uni.serval.ikora.core.builder.CallResolver;
import lu.uni.serval.ikora.core.model.*;
import lu.uni.serval.ikora.core.types.BaseTypeList;
import lu.uni.serval.ikora.core.types.KeywordType;
import lu.uni.serval.ikora.core.utils.ArgumentUtils;
import lu.uni.serval.ikora.core.runner.Runtime;

import java.util.ArrayList;
import java.util.List;

public class ArgumentResolver {
    private ArgumentResolver() {}

    public static void resolve(KeywordCall call, Runtime runtime) {
        for(Argument argument: call.getArgumentList()){
            resolveArgumentVariables(argument, runtime);
        }

        call.getKeyword().ifPresent(k -> resolveTypes(call, k, runtime));

        updateScope(call, runtime);
    }

    public static void resolveTypes(KeywordCall call, Keyword keyword, Runtime runtime){
        final NodeList<Argument> argumentList = call.getArgumentList();
        final BaseTypeList argumentTypes = keyword.getArgumentTypes();

        if(argumentTypes.containsType(KeywordType.class)){
            int keywordIndex = argumentTypes.findFirst(KeywordType.class);

            if(ArgumentUtils.isExpendedUntilPosition(argumentList, keywordIndex)){
                final List<Argument> argumentsToProcess = argumentList.subList(keywordIndex, argumentList.size());
                final KeywordCall callArgument = createKeywordArgument(argumentsToProcess);
                final NodeList<Argument> newArgumentList = new NodeList<>(argumentList.subList(0, keywordIndex));

                newArgumentList.add(new Argument(callArgument));
                call.setArgumentList(newArgumentList);

                CallResolver.resolve(callArgument, runtime);
            }
        }
    }

    private static KeywordCall createKeywordArgument(List<Argument> arguments) {
        final Argument keywordName = arguments.get(0);
        final KeywordCall call = new KeywordCall(keywordName.getDefinitionToken());

        final NodeList<Argument> argumentList = new NodeList<>();

        if(arguments.size() > 1){
            argumentList.addAll(arguments.subList(1, arguments.size()));
        }

        call.setArgumentList(argumentList);

        return call;
    }

    private static void resolveArgumentVariables(Argument argument, Runtime runtime) {
        final SourceNode definition = argument.getDefinition();
        final List<Variable> variables = new ArrayList<>();

        if(Variable.class.isAssignableFrom(definition.getClass())){
            variables.add((Variable)definition);
        }
        else if(Literal.class.isAssignableFrom(definition.getClass())){
            variables.addAll(((Literal)definition).getVariables());
        }

        variables.forEach(v -> VariableResolver.resolve(v, runtime));
    }

    private static void updateScope(KeywordCall call, Runtime runtime) {
        for(Keyword keyword: call.getAllPotentialKeywords(Link.Import.BOTH)){
            if(keyword instanceof ScopeModifier){
                ((ScopeModifier)keyword).addToScope(runtime, call);
            }
        }
    }
}
