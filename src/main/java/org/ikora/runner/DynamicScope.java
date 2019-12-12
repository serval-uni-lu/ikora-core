package org.ikora.runner;

import org.ikora.model.*;

import java.util.*;

public class DynamicScope implements Scope {
    private NodeTable<Variable> global;
    private Deque<Block<Suite, Variable>> suiteStack;
    private Deque<Block<TestCase, Variable>> testStack;
    private Deque<Block<Keyword, Variable>> keywordStack;
    private Deque<Block<Step, Value>> parametersStack;
    private List<Value> returnValues;

    public DynamicScope(){
        global = new NodeTable<>();
        suiteStack = new LinkedList<>();
        testStack = new LinkedList<>();
        keywordStack = new LinkedList<>();
        parametersStack = new LinkedList<>();
        returnValues = Collections.emptyList();
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
    public Set<Variable> findInScope(Set<TestCase> testCases, Set<String> suites, String name) {
        return null;
    }

    @Override
    public void addDynamicLibrary(KeywordDefinition keyword, List<Value> parameters) {

    }

    @Override
    public ResourcesTable getDynamicResources(Node node) {
        return null;
    }

    @Override
    public void enterKeyword(Keyword keyword) throws Exception {
        if (TestCase.class.isAssignableFrom(keyword.getClass())){
            testStack.push(new Block<>((TestCase) keyword));
        }
        else if(KeywordDefinition.class.isAssignableFrom(keyword.getClass())
                || LibraryKeyword.class.isAssignableFrom(keyword.getClass())){
            keywordStack.push(new Block<>(keyword));
        }
        else if(KeywordCall.class.isAssignableFrom(keyword.getClass())){
            parametersStack.push(new Block<>((Step) keyword));

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
        else if(KeywordDefinition.class.isAssignableFrom(keyword.getClass())
                || LibraryKeyword.class.isAssignableFrom(keyword.getClass())){
            keywordStack.pop();
            returnValues = keyword.getReturnValues();
        }
        else if(KeywordCall.class.isAssignableFrom(keyword.getClass())){
            parametersStack.pop();
            returnValues = keyword.getReturnValues();
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
    public List<Value> getReturnValues() {
        return returnValues;
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
