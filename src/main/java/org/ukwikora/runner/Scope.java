package org.ukwikora.runner;

import org.ukwikora.analytics.KeywordStatistics;
import org.ukwikora.model.*;

import java.io.File;
import java.util.*;

public class Scope {
    private StatementTable<Variable> global;
    private Map<TestCase, StatementTable<Variable>> test;
    private Map<String, StatementTable<Variable>> suite;

    private Map<KeywordDefinition, ResourcesTable> dynamicLibrary;

    public Scope(){
        global = new StatementTable<>();
        suite = new HashMap<>();
        test = new HashMap<>();
        dynamicLibrary = new HashMap<>();
    }

    public Set<Variable> findTestVariable(TestCase testCase, String name) {
        if(!test.containsKey(testCase)){
            return Collections.emptySet();
        }

        return test.get(testCase).findStatement(name);
    }

    public Set<Variable> findSuiteVariable(String suite, String name) {
        if(!this.suite.containsKey(suite)){
            return Collections.emptySet();
        }

        return this.suite.get(suite).findStatement(name);
    }

    public Set<Variable> findGlobalVariable(String name) {
        return global.findStatement(name);
    }

    public Set<Variable> findInScope(Set<TestCase> testCases, Set<String> suites, String name){
        Set<Variable> variablesFound = new HashSet<>();

        for(TestCase testCase: testCases){
            variablesFound.addAll(findTestVariable(testCase, name));
        }

        for(String suite: suites){
            variablesFound.addAll(findSuiteVariable(suite, name));
        }

        variablesFound.addAll(findGlobalVariable(name));

        return variablesFound;
    }

    public void addToGlobal(Variable variable) {
        global.add(variable);
    }

    public void addToSuite(String suite, Variable variable) {
        this.suite.putIfAbsent(suite, new StatementTable<>());
        this.suite.get(suite).add(variable);
    }

    public void addToTest(TestCase testCase, Variable variable) {
        test.putIfAbsent(testCase, new StatementTable<>());
        test.get(testCase).add(variable);
    }

    public void addDynamicLibrary(KeywordDefinition keyword, List<Value> parameters) {
        if(keyword == null){
            return;
        }

        if(parameters.isEmpty()){
            return;
        }

        File filePath = new File(parameters.get(0).getName());

        if(!filePath.isAbsolute() && keyword.getFile() != null) {
            filePath = new File(keyword.getFile().getFile(), filePath.getPath());
        }

        dynamicLibrary.putIfAbsent(keyword, new ResourcesTable());

        List<String> resourcesParameters = new ArrayList<>();
        for(int i = 1; i < parameters.size(); ++i){
            resourcesParameters.add(parameters.get(i).getName());
        }

        Resources resources = new Resources(parameters.get(0).getName(), filePath, resourcesParameters, "");
        dynamicLibrary.get(keyword).add(resources);
    }

    public ResourcesTable getDynamicResources(Statement statement) {
        Set<KeywordDefinition> dependencies = KeywordStatistics.getDependencies(statement, dynamicLibrary.keySet());

        ResourcesTable resourcesTable = new ResourcesTable();

        for(KeywordDefinition dependency: dependencies){
            for(Resources resources: dynamicLibrary.get(dependency)){
                resourcesTable.add(resources);
            }
        }

        return resourcesTable;
    }

    public void enterKeyword(Keyword keyword) {
    }

    public void exitKeyword(Keyword keyword) {
    }
}
