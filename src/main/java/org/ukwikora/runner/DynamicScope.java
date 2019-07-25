package org.ukwikora.runner;

import org.ukwikora.model.*;

import java.util.List;
import java.util.Set;
import java.util.Stack;

public class DynamicScope implements Scope {
    private StatementTable<Variable> global;
    private Stack<Block<String>> suiteStack;
    private Stack<Block<TestCase>> testStack;
    private Stack<Block<KeywordDefinition>> keywordStack;

    public DynamicScope(){
        global = new StatementTable<>();
        suiteStack = new Stack<>();
        testStack = new Stack<>();
        keywordStack = new Stack<>();
    }

    @Override
    public void addToTest(TestCase testCase, Variable variable) {
        if(testStack.peek().is(testCase)){
            testStack.peek().add(variable);
        }
    }

    @Override
    public void addToSuite(String suite, Variable variable) {
        if(suiteStack.peek().is(suite)){
            suiteStack.peek().add(variable);
        }
    }

    @Override
    public void addToGlobal(Variable variable) {
        global.add(variable);
    }

    @Override
    public Set<Variable> findInScope(Set<TestCase> testCases, Set<String> suites, String name) {
        return null;
    }

    @Override
    public void addDynamicLibrary(KeywordDefinition keyword, List<Value> parameters) {

    }

    @Override
    public ResourcesTable getDynamicResources(Statement statement) {
        return null;
    }

    @Override
    public void enterKeyword(Keyword keyword) {
        if (TestCase.class.isAssignableFrom(keyword.getClass())){
            testStack.push(new Block<>((TestCase) keyword));
        }
        else if(KeywordDefinition.class.isAssignableFrom(keyword.getClass())){
            keywordStack.push(new Block<>((KeywordDefinition)keyword));
        }
    }

    @Override
    public void exitKeyword(Keyword keyword) {
        if (TestCase.class.isAssignableFrom(keyword.getClass())){
            testStack.pop();
        }
        else if(KeywordDefinition.class.isAssignableFrom(keyword.getClass())){
            keywordStack.pop();
        }
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

    class Block<T>{
        T scope;
        StatementTable<Variable> variables;

        Block(T scope){
            this.scope = scope;
            this.variables = new StatementTable<>();
        }

        boolean is(Object other){
            return other == scope;
        }

        void add(Variable variable){
            variables.add(variable);
        }
    }
}
