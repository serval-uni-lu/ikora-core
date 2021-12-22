package lu.uni.serval.ikora.core.analytics.visitor;

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

import lu.uni.serval.ikora.core.model.*;

import java.util.*;

public class ValueVisitor extends EmptyVisitor {
    private final Map<Variable, Set<Value>> nodeToDefinitionMap = new HashMap<>();
    private final Deque<Variable> variableStack = new LinkedList<>();
    private final Deque<Argument> argumentStack = new LinkedList<>();

    public Map<Variable, Set<Value>> getVariableToValue() {
        return nodeToDefinitionMap;
    }

    private void addToMap(Variable variable, Value value){
        nodeToDefinitionMap.putIfAbsent(variable, new HashSet<>());
        nodeToDefinitionMap.get(variable).add(value);
    }

    @Override
    public void visit(Assignment assignment, VisitorMemory memory) {
        addToMap(variableStack.peek(), null);
    }

    @Override
    public void visit(UserKeyword keyword, VisitorMemory memory) {
        if(!argumentStack.isEmpty()){
            final Optional<Argument> argument = keyword.findArgument(argumentStack.peek().getDefinitionToken());

            if(argument.isPresent()){

                argumentStack.push(argument.get());
                VisitorUtils.traverseDependencies(this, keyword, memory);
                argumentStack.pop();
            }
        }

    }

    @Override
    public void visit(ScalarVariable scalar, VisitorMemory memory) {
        traverse(scalar, memory);
    }

    @Override
    public void visit(DictionaryVariable dictionary, VisitorMemory memory) {
        traverse(dictionary, memory);
    }

    @Override
    public void visit(ListVariable list, VisitorMemory memory) {
        traverse(list, memory);
    }

    @Override
    public void visit(KeywordCall call, VisitorMemory memory){
        if(!argumentStack.isEmpty()){
            final int position = argumentStack.peek().getPosition();

            if(position < call.getArgumentList().size()){
                final Argument argument = call.getArgumentList().get(position);
                visit(argument, memory);
            }
        }
    }

    @Override
    public void visit(Argument argument, VisitorMemory memory) {
        argumentStack.push(argument);
        argument.getDefinition().accept(this, memory);
        argumentStack.pop();
    }

    @Override
    public void visit(VariableAssignment assignment, VisitorMemory memory) {
        addToMap(variableStack.peek(), assignment.getVariable());
        variableStack.push(assignment.getVariable());

        for(Argument argument: assignment.getValues()){
            argument.accept(this, memory);
        }

        variableStack.pop();
    }

    @Override
    public void visit(Literal literal, VisitorMemory memory){
        if(memory.isAcceptable(literal)){
            if(!variableStack.isEmpty()){
                addToMap(variableStack.peek(), literal);
            }
            else {
                addToMap(null, literal);
            }

            for(Variable variable: literal.getVariables()){
                variableStack.push(null);
                variable.accept(this, memory);
                variableStack.pop();
            }
        }
    }

    private void traverse(Variable variable, VisitorMemory memory){
        if(memory.isAcceptable(variable)){
            memory = memory.getUpdated(variable);

            if(variableStack.peek() != null){
                addToMap(variableStack.peek(), variable);
            }

            variableStack.push(variable);

            for(Dependable dependable: variable.getDefinition(Link.Import.BOTH)){
                dependable.accept(this, memory);
            }

            variableStack.pop();
        }
    }
}
