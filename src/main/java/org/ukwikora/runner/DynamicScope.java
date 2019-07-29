package org.ukwikora.runner;

import org.ukwikora.model.*;

import java.util.*;

public class DynamicScope implements Scope {
    private StatementTable<Variable> global;
    private Stack<Block<Suite, Variable>> suiteStack;
    private Stack<Block<TestCase, Variable>> testStack;
    private Stack<Block<KeywordDefinition, Variable>> keywordStack;
    private Stack<Block<Step, Value>> parametersStack;

    public DynamicScope(){
        global = new StatementTable<>();
        suiteStack = new Stack<>();
        testStack = new Stack<>();
        keywordStack = new Stack<>();
        parametersStack = new Stack<>();
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
    public void enterKeyword(Keyword keyword) throws Exception {
        if (TestCase.class.isAssignableFrom(keyword.getClass())){
            testStack.push(new Block<>((TestCase) keyword));
        }
        else if(KeywordDefinition.class.isAssignableFrom(keyword.getClass())){
            keywordStack.push(new Block<>((KeywordDefinition)keyword));
        }
        else if(KeywordCall.class.isAssignableFrom(keyword.getClass())){
            parametersStack.push(new Block<>((Step)keyword));

            for(int i = 0; i < ((KeywordCall)keyword).getParameters().size(); ++i){
                Optional<Value> parameter = ((KeywordCall)keyword).getParameter(i, true);

                if(!parameter.isPresent()){
                    throw new Exception(String.format("Failed to resolve parameter '%s' for call '%s in '%s' at line %s",
                            ((KeywordCall)keyword).getParameter(i, false),
                            keyword.toString(),
                            keyword.getFileName(),
                            keyword.getLineRange().toString()));
                }

                parametersStack.peek().add(parameter.get());
            }
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
        else if(KeywordCall.class.isAssignableFrom(keyword.getClass())){
            parametersStack.pop();
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

    class Block<T, U>{
        T scope;
        List<U> variables;

        Block(T scope){
            this.scope = scope;
            this.variables = new ArrayList<U>();
        }

        boolean is(Object other){
            return other == scope;
        }

        void add(U variable){
            variables.add(variable);
        }
    }
}
