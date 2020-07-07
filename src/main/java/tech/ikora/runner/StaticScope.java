package tech.ikora.runner;

import tech.ikora.analytics.KeywordStatistics;
import tech.ikora.model.*;

import java.io.File;
import java.util.*;

public class StaticScope implements Scope{
    private SourceNodeTable<Variable> global;
    private Map<TestCase, SourceNodeTable<Variable>> test;
    private Map<String, SourceNodeTable<Variable>> suite;

    private Map<KeywordDefinition, ResourcesTable> dynamicLibrary;

    public StaticScope(){
        global = new SourceNodeTable<>();
        suite = new HashMap<>();
        test = new HashMap<>();
        dynamicLibrary = new HashMap<>();
    }

    @Override
    public Set<Node> findInScope(Set<TestCase> testCases, Set<String> suites, Token name){
        Set<Node> variablesFound = new HashSet<>();

        for(TestCase testCase: testCases){
            variablesFound.addAll(findTestVariable(testCase, name));
        }

        for(String suiteName: suites){
            variablesFound.addAll(findSuiteVariable(suiteName, name));
        }

        variablesFound.addAll(findGlobalVariable(name));

        return variablesFound;
    }

    @Override
    public void addToGlobal(Variable variable) {
        global.add(variable);
    }

    @Override
    public void addToSuite(String suite, Variable variable) {
        this.suite.putIfAbsent(suite, new SourceNodeTable<>());
        this.suite.get(suite).add(variable);
    }

    @Override
    public void addToKeyword(Keyword keyword, Variable variable) {

    }

    @Override
    public void addToTest(TestCase testCase, Variable variable) {
        test.putIfAbsent(testCase, new SourceNodeTable<>());
        test.get(testCase).add(variable);
    }

    @Override
    public void addDynamicLibrary(KeywordDefinition keyword, List<Argument> argumentList) {
        if(keyword == null){
            return;
        }

        if(argumentList.isEmpty()){
            return;
        }

        File filePath = new File(argumentList.get(0).getName());

        if(!filePath.isAbsolute() && keyword.getSourceFile() != null) {
            filePath = new File(keyword.getSourceFile().getFile(), filePath.getPath());
        }

        dynamicLibrary.putIfAbsent(keyword, new ResourcesTable());

        List<Token> resourcesParameters = new ArrayList<>();
        for(int i = 1; i < argumentList.size(); ++i){
            resourcesParameters.add(argumentList.get(i).getNameToken());
        }

        Resources resources = new Resources(argumentList.get(0).getNameToken(), filePath, resourcesParameters, Token.empty());
        dynamicLibrary.get(keyword).add(resources);
    }

    @Override
    public ResourcesTable getDynamicResources(Node node) {
        Set<KeywordDefinition> dependencies = KeywordStatistics.getDependencies(node, dynamicLibrary.keySet());

        ResourcesTable resourcesTable = new ResourcesTable();

        for(KeywordDefinition dependency: dependencies){
            for(Resources resources: dynamicLibrary.get(dependency)){
                resourcesTable.add(resources);
            }
        }

        return resourcesTable;
    }

    @Override
    public void enterNode(Node node) {
        // No concept of entering or leaving when performing static analysis
    }

    @Override
    public void exitNode(Node node) {
        // No concept of entering or leaving when performing static analysis
    }

    @Override
    public void enterSuite(Suite suite) {
        // No concept of entering or leaving when performing static analysis
    }

    @Override
    public void exitSuite(Suite suite) {
        // No concept of entering or leaving when performing static analysis
    }

    @Override
    public void reset() {
        global.clear();
        suite.clear();
        test.clear();
        dynamicLibrary.clear();
    }

    @Override
    public TestCase getTestCase() {
        return null;
    }

    @Override
    public NodeList<Value> getReturnValues() {
        return new NodeList<>();
    }

    private Set<Variable> findTestVariable(TestCase testCase, Token name) {
        if(!test.containsKey(testCase)){
            return Collections.emptySet();
        }

        return test.get(testCase).findNode(name);
    }


    private Set<Variable> findSuiteVariable(String suite, Token name) {
        if(!this.suite.containsKey(suite)){
            return Collections.emptySet();
        }

        return this.suite.get(suite).findNode(name);
    }


    private Set<Variable> findGlobalVariable(Token name) {
        return global.findNode(name);
    }
}
