package lu.uni.serval.ikora.core.builder.resolver;

import lu.uni.serval.ikora.core.builder.CallResolver;
import lu.uni.serval.ikora.core.error.ErrorMessages;
import lu.uni.serval.ikora.core.model.*;
import lu.uni.serval.ikora.core.types.*;
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

        boolean canResolve = true;
        int position = 0;
        for(; position < argumentList.size() && canResolve; ++position){
            final Argument argument = argumentList.get(position);

            if(position >= argumentTypes.size()){
                runtime.getErrors().registerSyntaxError(
                        call.getSource(),
                        String.format("%s: expected %d but got %d", ErrorMessages.TOO_MANY_KEYWORD_ARGUMENTS, argumentTypes.size(), argumentList.size()),
                        Range.fromToken(call.getDefinitionToken())
                );

                while (position < argumentList.size()){
                    argumentList.get(position++).setType(UnresolvedType.get());
                }

                break;
            }

            final BaseType type = argumentTypes.get(position);

            if(argument.isListVariable()){
                canResolve = assignType(type, ListType.class, argument);
            }
            else if(argument.isDictionaryVariable()){
                canResolve = assignType(type, DictionaryType.class, argument);
            }
            else if(KeywordType.class.isAssignableFrom(type.getClass())){
                final List<Argument> argumentsToProcess = argumentList.subList(position, argumentList.size());
                final KeywordCall callArgument = createKeywordArgument(argumentsToProcess);
                final NodeList<Argument> newArgumentList = new NodeList<>(argumentList.subList(0, position));

                newArgumentList.add(new Argument(callArgument, type, position));
                call.setArgumentList(newArgumentList);

                CallResolver.resolve(callArgument, runtime);

                break;
            }
            else {
                argument.setType(type);
            }
        }
    }

    private static boolean assignType(BaseType type, Class<? extends BaseType> expected, Argument argument){
        boolean canResolve = true;

        if(type.getClass().isAssignableFrom(expected)){
            argument.setType(type);
        }
        else {
            argument.setType(UnresolvedType.get());
            canResolve = false;
        }

        return canResolve;
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
