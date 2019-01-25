package org.ukwikora.model;

import java.util.*;

public class Scope {
    private StatementTable<Variable> globalScope;
    private Map<TestCase, StatementTable<Variable>> testScope;
    private Map<String, StatementTable<Variable>> suiteScope;

    public Scope(){
        globalScope = new StatementTable<>();
        suiteScope = new HashMap<>();
        testScope = new HashMap<>();
    }

    public Optional<Variable> findTestVariable(TestCase testCase, String name) {
        if(!testScope.containsKey(testCase)){
            return Optional.empty();
        }

        return Optional.of(testScope.get(testCase).findStatement(name));
    }

    public Optional<Variable> findSuiteVariable(String suite, String name) {
        if(!suiteScope.containsKey(suite)){
            return Optional.empty();
        }

        return Optional.of(suiteScope.get(suite).findStatement(name));
    }

    public Variable findGlobalVariable(String name) {
        return globalScope.findStatement(name);
    }

    public Optional<Variable> findInScope(Set<TestCase> testCases, List<String> suites, String name){
        for(TestCase testCase: testCases){
            Optional<Variable> variable = findTestVariable(testCase, name);
            if(variable.isPresent()){
                return variable;
            }
        }

        for(String suite: suites){
            Optional<Variable> variable = findSuiteVariable(suite, name);
            if(variable.isPresent()){
                return variable;
            }
        }

        return Optional.ofNullable(findGlobalVariable(name));
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
}
