/*
 *
 *     Copyright © 2019 - 2022 University of Luxembourg
 *
 *     Licensed under the Apache License, Version 2.0 (the "License")
 *     you may not use this file except in compliance with the License.
 *     You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *     Unless required by applicable law or agreed to in writing, software
 *     distributed under the License is distributed on an "AS IS" BASIS,
 *     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *     See the License for the specific language governing permissions and
 *     limitations under the License.
 */
package lu.uni.serval.ikora.core.analytics.resolver;

import lu.uni.serval.ikora.core.error.ErrorManager;
import lu.uni.serval.ikora.core.error.ErrorMessages;
import lu.uni.serval.ikora.core.model.*;
import lu.uni.serval.ikora.core.types.*;

import java.util.ArrayList;
import java.util.List;

public class ArgumentResolver {
    private ArgumentResolver() {}

    public static void resolve(StaticScope scope, Argument argument) {
        final List<Variable> variables = new ArrayList<>();

        if(argument.isVariable()){
            variables.add((Variable)argument.getDefinition());
        }
        else if(argument.isLiteral()){
            variables.addAll(((Literal)argument.getDefinition()).getVariables());
        }

        variables.forEach(v -> VariableResolver.resolve(scope, v));
    }

    public static void resolve(StaticScope staticScope, KeywordCall call, ErrorManager errorManager) {
        for(Argument argument: call.getArgumentList()){
            resolve(staticScope, argument);
        }

        call.getKeyword().ifPresent(k -> resolveTypes(staticScope, call, k, errorManager));

        updateScope(staticScope, call, errorManager);
    }

    private static void resolveTypes(StaticScope staticScope, KeywordCall call, Keyword keyword, ErrorManager errorManager){
        final NodeList<Argument> argumentList = call.getArgumentList();
        final BaseTypeList argumentTypes = keyword.getArgumentTypes();

        boolean canResolve = true;
        int position = 0;
        for(; position < argumentList.size() && canResolve; ++position){
            final Argument argument = argumentList.get(position);

            if(position >= argumentTypes.size()){
                errorManager.registerSyntaxError(
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
                position = assignKeywordType(staticScope, type, call, argumentList, position, argumentList.size(), errorManager);
            }
            else if(KeywordListType.class.isAssignableFrom(type.getClass())){
                position = assignKeywordListType(staticScope, type, call, argumentList, position, errorManager);
            }
            else {
                argument.setType(type);
            }
        }
    }

    private static int assignKeywordListType(StaticScope staticScope, BaseType type, KeywordCall call, NodeList<Argument> argumentList, int position, ErrorManager errorManager){
        final NodeList<Argument> newArgumentList = new NodeList<>(argumentList.subList(0, position));
        call.setArgumentList(newArgumentList);

        int start = position;

        while (position < argumentList.size()){
            if(argumentList.get(position).getDefinition().getName().equalsIgnoreCase("and")) {
                final List<Argument> argumentsToProcess = argumentList.subList(start, position);
                final KeywordCall callArgument = createKeywordArgument(argumentsToProcess);
                newArgumentList.add(new Argument(callArgument, new KeywordType(type.getName()), start));

                CallResolver.resolve(staticScope, callArgument, errorManager);
                start = position + 1;
            }

            ++position;
        }

        if(start < position){
            final List<Argument> argumentsToProcess = argumentList.subList(start, position);
            final KeywordCall callArgument = createKeywordArgument(argumentsToProcess);
            newArgumentList.add(new Argument(callArgument, new KeywordType(type.getName()), start));

            CallResolver.resolve(staticScope, callArgument, errorManager);
        }

        return position;
    }

    private static int assignKeywordType(StaticScope staticScope, BaseType type, KeywordCall call, NodeList<Argument> argumentList, int start, int end, ErrorManager errorManager){
        final List<Argument> argumentsToProcess = argumentList.subList(start, end);
        final KeywordCall callArgument = createKeywordArgument(argumentsToProcess);
        final NodeList<Argument> newArgumentList = new NodeList<>(argumentList.subList(0, start));

        newArgumentList.add(new Argument(callArgument, type, start));
        call.setArgumentList(newArgumentList);

        CallResolver.resolve(staticScope, callArgument, errorManager);

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

    private static void updateScope(StaticScope staticScope, KeywordCall call, ErrorManager errorManager) {
        for(Keyword keyword: call.getAllPotentialKeywords(Link.Import.BOTH)){
            if(keyword instanceof ScopeModifier scopeModifier){
                scopeModifier.modifyScope(staticScope, call, errorManager);
            }
        }
    }
}
