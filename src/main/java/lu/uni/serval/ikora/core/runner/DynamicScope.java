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

import java.util.*;

public class DynamicScope implements ScopeManager {
    private final SourceNodeTable<Variable> global;
    private final Deque<Block<Suite, Variable>> suiteStack;
    private final Deque<Block<TestCase, Variable>> testStack;
    private final Deque<Block<Keyword, Variable>> keywordStack;
    private final Deque<List<Resolved>> argumentStack;
    private NodeList<Value> returnValues;

    public DynamicScope(){
        global = new SourceNodeTable<>();
        suiteStack = new LinkedList<>();
        testStack = new LinkedList<>();
        keywordStack = new LinkedList<>();
        argumentStack = new LinkedList<>();
        returnValues = new NodeList<>();
    }

    public void reset(){
        global.clear();
        suiteStack.clear();
        testStack.clear();
        keywordStack.clear();
        argumentStack.clear();
        returnValues.clear();
    }

    public void addToKeyword(Keyword keyword, Variable variable) {
        Block<Keyword, Variable> block = keywordStack.peek();

        if(block != null && block.is(keyword)){
            block.add(variable);
        }
    }


    public void addToArguments(List<Resolved> arguments){
        argumentStack.add(arguments);
    }

    @Override
    public void addToTestScope(TestCase testCase, Variable variable) {
        Block<TestCase, Variable> block = testStack.peek();

        if(block != null && block.is(testCase)){
            block.add(variable);
        }
    }

    @Override
    public void addLibraryToScope(KeywordDefinition keyword, List<Argument> argumentList) {

    }

    @Override
    public void addToSuiteScope(String suite, Variable variable) {
        Block<Suite, Variable> block = suiteStack.peek();

        if(block != null && block.is(suite)){
            block.add(variable);
        }
    }

    @Override
    public void addToGlobalScope(Variable variable) {
        global.add(variable);
    }

    public Set<Node> find(Variable variable) {
        final Set<Node> found = new HashSet<>();

        final SourceFile sourceFile = variable.getSourceFile();
        final List<VariableAssignment> variableAssignmentList = sourceFile.getVariables();

        for(VariableAssignment variableAssignment: variableAssignmentList){
            if(variableAssignment.matches(variable.getDefinitionToken())){
                found.add(variableAssignment);
            }
        }

        return found;
    }

    public void addDynamicLibrary(KeywordDefinition keyword, List<Argument> argumentList) {
        throw new UnsupportedOperationException();
    }

    public ResourcesTable getDynamicResources(Node node) {
        return null;
    }

    public void enterNode(Node node) {
        if (TestCase.class.isAssignableFrom(node.getClass())){
            testStack.push(new Block<>((TestCase) node));
        }
        else if(Keyword.class.isAssignableFrom(node.getClass())){
            keywordStack.push(new Block<>((Keyword) node));
        }
    }

    public void exitNode(Node node) {
        if (TestCase.class.isAssignableFrom(node.getClass())){
            testStack.pop();
        }
        else if(Keyword.class.isAssignableFrom(node.getClass())){
            keywordStack.pop();
        }
        else if(KeywordCall.class.isAssignableFrom(node.getClass())){
            argumentStack.pop();
        }
    }

    public void enterSuite(Suite suite) {
        suiteStack.push(new Block<>(suite));
    }

    public void exitSuite(Suite suite) {
        suiteStack.pop();
    }

    public Optional<TestCase> getTestCase() {
        return testStack.peek() != null ? Optional.of(testStack.peek().scope) : Optional.empty();
    }

    public NodeList<Value> getReturnValues() {
        return returnValues;
    }

    public List<Resolved> getArguments() {
        return argumentStack.peek() != null ? argumentStack.peek() : Collections.emptyList();
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
