package lu.uni.serval.ikora.core.utils;

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

import lu.uni.serval.ikora.core.builder.resolver.ValueResolver;
import lu.uni.serval.ikora.core.model.*;
import lu.uni.serval.ikora.core.types.BaseType;
import lu.uni.serval.ikora.core.types.BaseTypeList;
import lu.uni.serval.ikora.core.types.KeywordType;
import lu.uni.serval.ikora.core.types.StringType;
import org.apache.commons.lang3.tuple.Pair;

import java.util.*;
import java.util.stream.Collectors;

public class ArgumentUtils {
    private ArgumentUtils() {}

    public static boolean contains(NodeList<Argument> arguments, Value value){
        return arguments.stream().map(Argument::getDefinition).anyMatch(v -> v == value);
    }

    public static Class<? extends BaseType> getArgumentType(Argument argument){
        SourceNode parent = argument.getAstParent(true);

        if(parent == null || parent.getClass() != KeywordCall.class){
            return StringType.class;
        }

        final Optional<Keyword> keyword = ((KeywordCall)parent).getKeyword();

        if(!keyword.isPresent()){
            return StringType.class;
        }

        final BaseTypeList argumentTypes = keyword.get().getArgumentTypes();
        int index = ((KeywordCall) parent).getArgumentList().indexOf(argument);

        if(index < 0 || index >= argumentTypes.size()){
            return StringType.class;
        }

        return argumentTypes.get(index).getClass();
    }

    public static List<Pair<String, SourceNode>> getArgumentValues(Argument argument){
        final List<Pair<String, SourceNode>> values = new ArrayList<>();

        for(Node node: ValueResolver.getValueNodes(argument)){
            if(node instanceof Literal){
                values.add(Pair.of(node.getName(), (SourceNode) node));
            }
            else if(node instanceof Argument){
                values.add(Pair.of(node.getName(), ((Argument)node).getDefinition()));
            }
            else if (node instanceof LibraryVariable){
                values.add(Pair.of(node.getName(), argument));
            }
            else if(node instanceof VariableAssignment){
                values.addAll(getAssignmentValue((VariableAssignment)node));
            }
        }

        return values;
    }

    private static List<Pair<String, SourceNode>> getAssignmentValue(final VariableAssignment assignment){
        final List<List<String>> values = new ArrayList<>();

        for(Argument argument: assignment.getValues()){
            values.add(getArgumentValues(argument).stream().map(Pair::getLeft).collect(Collectors.toList()));
        }

        return Permutations.permutations(values).stream()
                .map(v -> Pair.of(String.join("\t", v), (SourceNode)assignment))
                .collect(Collectors.toList());
    }

    public static NodeList<Value> toValues(int offset, List<Argument> argumentList){
        final NodeList<Value> values = new NodeList<>();

        for(Argument argument: argumentList.subList(offset, argumentList.size())){
            if(!argument.isType(KeywordType.class)){
                values.add((Value) argument.getDefinition());
            }
        }

        return values;
    }
}
