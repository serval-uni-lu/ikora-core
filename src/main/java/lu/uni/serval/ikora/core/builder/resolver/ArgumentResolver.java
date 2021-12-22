package lu.uni.serval.ikora.core.builder.resolver;

/*-
 * #%L
 * Ikora Core
 * %%
 * Copyright (C) 2019 - 2021 University of Luxembourg
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License")
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import lu.uni.serval.ikora.core.error.ErrorMessages;
import lu.uni.serval.ikora.core.model.*;
import lu.uni.serval.ikora.core.types.*;
import lu.uni.serval.ikora.core.runner.Runtime;

import java.util.ArrayList;
import java.util.List;

public class ArgumentResolver {
    private ArgumentResolver() {}

    public static void resolve(Argument argument, Runtime runtime) {
        final List<Variable> variables = new ArrayList<>();

        if(argument.isVariable()){
            variables.add((Variable)argument.getDefinition());
        }
        else if(argument.isLiteral()){
            variables.addAll(((Literal)argument.getDefinition()).getVariables());
        }

        variables.forEach(v -> VariableResolver.resolve(v, runtime));
    }

    public static void resolve(KeywordCall call, Runtime runtime) {
        for(Argument argument: call.getArgumentList()){
            resolve(argument, runtime);
        }

        call.getKeyword().ifPresent(k -> resolveTypes(call, k, runtime));

        updateScope(call, runtime);
    }

    private static void resolveTypes(KeywordCall call, Keyword keyword, Runtime runtime){
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
                position = assignKeywordType(type, call, argumentList, position, argumentList.size(), runtime);
            }
            else if(KeywordListType.class.isAssignableFrom(type.getClass())){
                position = assignKeywordListType(type, call, argumentList, position, runtime);
            }
            else {
                argument.setType(type);
            }
        }
    }

    private static int assignKeywordListType(BaseType type, KeywordCall call, NodeList<Argument> argumentList, int position, Runtime runtime){
        final NodeList<Argument> newArgumentList = new NodeList<>(argumentList.subList(0, position));
        call.setArgumentList(newArgumentList);

        int start = position;

        while (position < argumentList.size()){
            if(argumentList.get(position).getDefinition().getName().equalsIgnoreCase("and")) {
                final List<Argument> argumentsToProcess = argumentList.subList(start, position);
                final KeywordCall callArgument = createKeywordArgument(argumentsToProcess);
                newArgumentList.add(new Argument(callArgument, new KeywordType(type.getName()), start));

                CallResolver.resolve(callArgument, runtime);
                start = position + 1;
            }

            ++position;
        }

        if(start < position){
            final List<Argument> argumentsToProcess = argumentList.subList(start, position);
            final KeywordCall callArgument = createKeywordArgument(argumentsToProcess);
            newArgumentList.add(new Argument(callArgument, new KeywordType(type.getName()), start));

            CallResolver.resolve(callArgument, runtime);
        }

        return position;
    }

    private static int assignKeywordType(BaseType type, KeywordCall call, NodeList<Argument> argumentList, int start, int end, Runtime runtime){
        final List<Argument> argumentsToProcess = argumentList.subList(start, end);
        final KeywordCall callArgument = createKeywordArgument(argumentsToProcess);
        final NodeList<Argument> newArgumentList = new NodeList<>(argumentList.subList(0, start));

        newArgumentList.add(new Argument(callArgument, type, start));
        call.setArgumentList(newArgumentList);

        CallResolver.resolve(callArgument, runtime);

        return end;
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

    private static void updateScope(KeywordCall call, Runtime runtime) {
        for(Keyword keyword: call.getAllPotentialKeywords(Link.Import.BOTH)){
            if(keyword instanceof ScopeModifier){
                ((ScopeModifier)keyword).addToScope(runtime, call);
            }
        }
    }
}
