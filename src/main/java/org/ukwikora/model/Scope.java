package org.ukwikora.model;

import org.ukwikora.analytics.KeywordStatistics;

import java.io.File;
import java.util.*;

public class Scope {
    private StatementTable<Variable> globalScope;
    private Map<TestCase, StatementTable<Variable>> testScope;
    private Map<String, StatementTable<Variable>> suiteScope;
    private Map<KeywordDefinition, ResourcesTable> dynamicResourcesScope;

    public Scope(){
        globalScope = new StatementTable<>();
        suiteScope = new HashMap<>();
        testScope = new HashMap<>();
        dynamicResourcesScope = new HashMap<>();
    }

    public Set<Variable> findTestVariable(TestCase testCase, String name) {
        if(!testScope.containsKey(testCase)){
            return Collections.emptySet();
        }

        return testScope.get(testCase).findStatement(name);
    }

    public Set<Variable> findSuiteVariable(String suite, String name) {
        if(!suiteScope.containsKey(suite)){
            return Collections.emptySet();
        }

        return suiteScope.get(suite).findStatement(name);
    }

    public Set<Variable> findGlobalVariable(String name) {
        return globalScope.findStatement(name);
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

    public void setGlobalScope(Variable variable) {
        globalScope.add(variable);
    }

    public void setSuiteScope(String suite, Variable variable) {
        suiteScope.putIfAbsent(suite, new StatementTable<>());
        suiteScope.get(suite).add(variable);
    }

    public void setTestScope(TestCase testCase, Variable variable) {
        testScope.putIfAbsent(testCase, new StatementTable<>());
        testScope.get(testCase).add(variable);
    }

    public void setDynamicResources(KeywordDefinition keyword, List<Value> parameters) {
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

        dynamicResourcesScope.putIfAbsent(keyword, new ResourcesTable());

        List<String> resourcesParameters = new ArrayList<>();
        for(int i = 1; i < parameters.size(); ++i){
            resourcesParameters.add(parameters.get(i).getName());
        }

        Resources resources = new Resources(parameters.get(0).getName(), filePath, resourcesParameters, "");
        dynamicResourcesScope.get(keyword).add(resources);
    }

    public ResourcesTable getDynamicResources(Statement statement) {
        Set<KeywordDefinition> dependencies = KeywordStatistics.getDependencies(statement, dynamicResourcesScope.keySet());

        ResourcesTable resourcesTable = new ResourcesTable();

        for(KeywordDefinition dependency: dependencies){
            for(Resources resources: dynamicResourcesScope.get(dependency)){
                resourcesTable.add(resources);
            }
        }

        return resourcesTable;
    }
}
