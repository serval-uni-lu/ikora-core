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

import lu.uni.serval.ikora.core.analytics.visitor.PathMemory;
import lu.uni.serval.ikora.core.analytics.visitor.ValueVisitor;
import lu.uni.serval.ikora.core.analytics.visitor.VisitorMemory;
import lu.uni.serval.ikora.core.model.*;
import lu.uni.serval.ikora.core.types.KeywordType;

import java.util.*;

public class ArgumentUtils {
    private ArgumentUtils() {}

    public static boolean contains(NodeList<Argument> arguments, Value value){
        return arguments.stream().map(Argument::getDefinition).anyMatch(v -> v == value);
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

    public static Map<SourceNode, Set<Node>> getValues(Argument argument) {
        final VisitorMemory memory = new PathMemory();
        final ValueVisitor visitor = new ValueVisitor();

        visitor.visit(argument, memory);

        return visitor.getNodeToDefinitionMap();
    }
}
