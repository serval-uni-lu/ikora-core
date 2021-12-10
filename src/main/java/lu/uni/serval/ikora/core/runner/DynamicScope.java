package lu.uni.serval.ikora.core.runner;

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
import org.apache.commons.lang3.NotImplementedException;

import java.util.*;

public class DynamicScope implements Scope {
    private final SourceNodeTable<Variable> global;
    private final Deque<Block<Suite, Variable>> suiteStack;
    private final Deque<Block<TestCase, Variable>> testStack;
    private final Deque<Block<Keyword, Variable>> keywordStack;
    private final Deque<Block<Step, Argument>> argumentStack;
    private NodeList<Value> returnValues;

    public DynamicScope(){
        global = new SourceNodeTable<>();
        suiteStack = new LinkedList<>();
        testStack = new LinkedList<>();
        keywordStack = new LinkedList<>();
        argumentStack = new LinkedList<>();
        returnValues = new NodeList<>();
    }

    @Override
    public void addToKeyword(Keyword keyword, Variable variable) {
        Block<Keyword, Variable> block = keywordStack.peek();

        if(block != null && block.is(keyword)){
            block.add(variable);
        }
    }

    @Override
    public void addToTest(TestCase testCase, Variable variable) {
        Block<TestCase, Variable> block = testStack.peek();

        if(block != null && block.is(testCase)){
            block.add(variable);
        }
    }

    @Override
    public void addToSuite(String suite, Variable variable) {
        Block<Suite, Variable> block = suiteStack.peek();

        if(block != null && block.is(suite)){
            block.add(variable);
        }
    }

    @Override
    public void addToGlobal(Variable variable) {
        global.add(variable);
    }

    @Override
    public Set<Node> findInScope(Set<TestCase> testCases, Set<String> suites, Token name) {
        throw new NotImplementedException("Runner is not implemented yet");
    }

    @Override
    public void addDynamicLibrary(KeywordDefinition keyword, List<Argument> argumentList) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ResourcesTable getDynamicResources(Node node) {
        return null;
    }

    @Override
    public void enterNode(Node node) {
        if (TestCase.class.isAssignableFrom(node.getClass())){
            testStack.push(new Block<>((TestCase) node));
        }
        else if(Keyword.class.isAssignableFrom(node.getClass())){
            keywordStack.push(new Block<>((Keyword) node));
        }
        else if(KeywordCall.class.isAssignableFrom(node.getClass())){
            argumentStack.push(new Block<>((Step) node));

            for(Argument argument: ((KeywordCall) node).getArgumentList()){
                argumentStack.peek().add(argument);
            }
        }
    }

    @Override
    public void exitNode(Node node) {
        if (TestCase.class.isAssignableFrom(node.getClass())){
            testStack.pop();
        }
        else if(Keyword.class.isAssignableFrom(node.getClass())){
            keywordStack.pop();
            returnValues = ((Keyword) node).getReturnValues();
        }
        else if(KeywordCall.class.isAssignableFrom(node.getClass())){
            argumentStack.pop();
        }
    }

    @Override
    public void enterSuite(Suite suite) {
        suiteStack.push(new Block<>(suite));
    }

    @Override
    public void exitSuite(Suite suite) {
        suiteStack.pop();
    }

    @Override
    public void reset() {
        global.clear();
        suiteStack.clear();
        testStack.clear();
        keywordStack.clear();
    }

    @Override
    public TestCase getTestCase() {
        return testStack.peek().scope;
    }

    @Override
    public NodeList<Value> getReturnValues() {
        return returnValues;
    }

    class Block<T, U>{
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
