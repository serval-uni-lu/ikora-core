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

import java.util.*;

public class ValueFetcher {
    private ValueFetcher() {}

    public static Set<String> getValues(Argument argument) {
        final VisitorMemory memory = new PathMemory();
        final ValueVisitor visitor = new ValueVisitor();

        visitor.visit(argument, memory);

        final Map<Variable, Set<Value>> variableToValue = visitor.getVariableToValue();

        Set<Value> initial;

        if(argument.isVariable()){
            initial = variableToValue.getOrDefault((Variable) argument.getDefinition(), Collections.emptySet());
        }
        else {
            initial = variableToValue.getOrDefault(null, Collections.emptySet());
        }

        return fillValues(initial, variableToValue);
    }

    private static Set<String> fillValues(Set<Value> values, Map<Variable, Set<Value>> variableToValue){
        final Set<String> resolved = new HashSet<>();

        for(Value value: values){
            if(value == null){
                continue;
            }

            if(value.isLiteral()){
                resolved.addAll(fillValues((Literal)value, variableToValue));
            }
            else if(value.isVariable()){
                resolved.addAll(fillValues((Variable) value, variableToValue));
            }
        }

        return resolved;
    }

    private static Set<String> fillValues(Literal literal, Map<Variable, Set<Value>> variableToValue){
        final Set<String> resolved = new HashSet<>();
        final List<Variable> variables = literal.getVariables();

        if(variables.isEmpty()){
            resolved.add(literal.getName());
        }
        else {
            final List<Set<String>> values = new ArrayList<>();

            for(Variable variable: variables){
                values.add(fillValues(variable, variableToValue));
            }

            if (values.stream().noneMatch(Set::isEmpty)) {
                for(List<String> toFill: Permutations.permutations(values)) {
                    final String filled = fillLiteral(literal, variables, toFill);
                    resolved.add(filled);
                }
            }
        }

        return resolved;
    }

    private static Set<String> fillValues(Variable variable, Map<Variable, Set<Value>> variableToValue){
        return fillValues(variableToValue.getOrDefault(variable, Collections.emptySet()), variableToValue);
    }

    private static String fillLiteral(Literal literal, List<Variable> variables, List<String> toFill){
        if(variables.size() != toFill.size()){
            throw new ArrayIndexOutOfBoundsException(String.format("Expected %d but got %d", variables.size(), toFill.size()));
        }

        Token resolved = literal.getDefinitionToken();

        for(int position = variables.size() - 1; position >= 0; --position){
            final Variable variable = variables.get(position);
            final String value = toFill.get(position);

            resolved = resolved.replace(variable.getDefinitionToken(), value);
        }

        return resolved.getText();
    }
}
