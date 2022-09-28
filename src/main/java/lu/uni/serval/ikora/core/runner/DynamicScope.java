/*
 *
 *     Copyright © 2022 University of Luxembourg
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
package lu.uni.serval.ikora.core.runner;

import lu.uni.serval.ikora.core.model.*;
import lu.uni.serval.ikora.core.runner.exception.InternalException;

import java.util.*;

public class DynamicScope {
    private final SourceNodeTable<VariableAssignment> global;
    private final Deque<Block<Suite, VariableAssignment>> suiteStack;
    private final Deque<Block<TestCase, VariableAssignment>> testStack;
    private final Deque<Block<Keyword, VariableAssignment>> keywordStack;
    private final Deque<List<Resolved>> argumentStack;
    private List<Value> returnValues;

    public DynamicScope(){
        global = new SourceNodeTable<>();
        suiteStack = new LinkedList<>();
        testStack = new LinkedList<>();
        keywordStack = new LinkedList<>();
        argumentStack = new LinkedList<>();
        returnValues = Collections.emptyList();
    }

    public void reset(){
        global.clear();
        suiteStack.clear();
        testStack.clear();
        keywordStack.clear();
        argumentStack.clear();
        returnValues.clear();
    }

    public void addToKeyword(VariableAssignment variableAssignment) {
        Block<Keyword, VariableAssignment> block = keywordStack.peek();

        if(block != null){
            block.add(variableAssignment);
        }
    }


    public void addToArguments(List<Resolved> arguments) throws InternalException {
        final List<Resolved> block = argumentStack.peek();

        if(block == null){
            throw new InternalException("Expected to be within the scope of a test");
        }

        block.addAll(arguments);
    }

    public void addToTestScope(VariableAssignment variable) throws InternalException {
        Block<TestCase, VariableAssignment> block = testStack.peek();

        if(block == null){
            throw new InternalException("Expected to be within the scope of a test");
        }

        block.add(variable);
    }

    public void addToSuiteScope(VariableAssignment variable) throws InternalException {
        Block<Suite, VariableAssignment> block = suiteStack.peek();

        if(block == null){
            throw new InternalException("Expected to be within the scope of a suite");
        }

        block.add(variable);
    }

    public void addToGlobalScope(VariableAssignment variable) {
        global.add(variable);
    }

    public Set<Node> findInScope(Variable variable) {
        final Set<Node> found = new HashSet<>();

        // 1. Check if it was defined in the keyword
        Optional<VariableAssignment> inScope = findInScope(Keyword.class, variable);
        if(inScope.isPresent()){
            found.add(inScope.get());
            return found;
        }

        // 2. Check if it is a test variable
        inScope = findInScope(TestCase.class, variable);
        if(inScope.isPresent()){
            found.add(inScope.get());
            return found;
        }

        // 3. Check if it is a suite variable
        inScope = findInScope(Suite.class, variable);
        if(inScope.isPresent()){
            found.add(inScope.get());
            return found;
        }

        // 4. Check if it is defined in a variable block
        final SourceFile sourceFile = variable.getSourceFile();
        final List<VariableAssignment> variableAssignmentList = sourceFile.getVariables();

        for(VariableAssignment variableAssignment: variableAssignmentList){
            if(variableAssignment.matches(variable.getDefinitionToken())){
                found.add(variableAssignment);
            }
        }

        return found;
    }

    public void enterNode(Node node) {
        if (TestCase.class.isAssignableFrom(node.getClass())){
            testStack.push(new Block<>((TestCase) node));
            keywordStack.push(new Block<>((Keyword) node));
        }
        else if(Keyword.class.isAssignableFrom(node.getClass())){
            keywordStack.push(new Block<>((Keyword) node));
        }
        else if(Step.class.isAssignableFrom(node.getClass())){
            argumentStack.push(new ArrayList<>());
        }
    }

    public void exitNode(Node node) {
        if (TestCase.class.isAssignableFrom(node.getClass())){
            testStack.pop();
            keywordStack.pop();
        }
        else if(Keyword.class.isAssignableFrom(node.getClass())){
            keywordStack.pop();
        }
        else if(Step.class.isAssignableFrom(node.getClass())){
            argumentStack.pop();
        }
    }

    public void enterSuite(Suite suite) {
        suiteStack.push(new Block<>(suite));
    }

    public void exitSuite() {
        suiteStack.pop();
    }

    public Optional<TestCase> getTestCase() {
        return testStack.peek() != null ? Optional.of(testStack.peek().scope) : Optional.empty();
    }

    public List<Value> getReturnValues() {
        return returnValues;
    }

    public List<Resolved> getArguments() {
        return argumentStack.peek() != null ? argumentStack.peek() : Collections.emptyList();
    }

    private Optional<VariableAssignment> findInScope(Class<?> scope, Variable variable) {
        if(TestCase.class.isAssignableFrom(scope) && !testStack.isEmpty()) {
            return testStack.peek().variables.stream()
                    .filter(v -> v.getVariable().matches(variable.getDefinitionToken()))
                    .findAny();
        }

        if (Keyword.class.isAssignableFrom(scope) && !keywordStack.isEmpty()){
            return keywordStack.peek().variables.stream()
                    .filter(v -> v.getVariable().matches(variable.getDefinitionToken()))
                    .findAny();
        }

        return Optional.empty();
    }

    public void setReturnValues(List<Value> values) {
        returnValues = values;
    }

    static class Block<T, U>{
        T scope;
        List<U> variables;

        Block(T scope){
            this.scope = scope;
            this.variables = new ArrayList<>();
        }

        boolean is(Object other){
            return other == scope;
        }

        void add(U variable){
            variables.add(variable);
        }
    }
}
