package org.ikora.runner;

import org.ikora.analytics.KeywordStatistics;
import org.ikora.model.*;

import java.io.File;
import java.util.*;

public class StaticScope implements Scope{
    private NodeTable<Variable> global;
    private Map<TestCase, NodeTable<Variable>> test;
    private Map<String, NodeTable<Variable>> suite;

    private Map<KeywordDefinition, ResourcesTable> dynamicLibrary;

    public StaticScope(){
        global = new NodeTable<>();
        suite = new HashMap<>();
        test = new HashMap<>();
        dynamicLibrary = new HashMap<>();
    }

    @Override
    public Set<Variable> findInScope(Set<TestCase> testCases, Set<String> suites, String name){
        Set<Variable> variablesFound = new HashSet<>();

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
        this.suite.putIfAbsent(suite, new NodeTable<>());
        this.suite.get(suite).add(variable);
    }

    @Override
    public void addToKeyword(Keyword keyword, Variable variable) {

    }

    @Override
    public void addToTest(TestCase testCase, Variable variable) {
        test.putIfAbsent(testCase, new NodeTable<>());
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

        List<String> resourcesParameters = new ArrayList<>();
        for(int i = 1; i < argumentList.size(); ++i){
            resourcesParameters.add(argumentList.get(i).getName());
        }

        Resources resources = new Resources(argumentList.get(0).getName(), filePath, resourcesParameters, "");
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
    public List<Value> getReturnValues() {
        return Collections.emptyList();
    }

    private Set<Variable> findTestVariable(TestCase testCase, String name) {
        if(!test.containsKey(testCase)){
            return Collections.emptySet();
        }

        return test.get(testCase).findNode(name);
    }


    private Set<Variable> findSuiteVariable(String suite, String name) {
        if(!this.suite.containsKey(suite)){
            return Collections.emptySet();
        }

        return this.suite.get(suite).findNode(name);
    }


    private Set<Variable> findGlobalVariable(String name) {
        return global.findNode(name);
    }
}