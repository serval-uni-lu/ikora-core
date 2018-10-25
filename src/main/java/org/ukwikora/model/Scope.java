package org.ukwikora.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class Scope {
    private ElementTable<Variable> globalScope;
    private Map<TestCase, ElementTable<Variable>> testScope;
    private Map<String, ElementTable<Variable>> suiteScope;

    public Scope(){
        globalScope = new ElementTable<>();
        suiteScope = new HashMap<>();
        testScope = new HashMap<>();
    }

    public Optional<Variable> findTestVariable(TestCase testCase, String name) {
        if(!testScope.containsKey(testCase)){
            return Optional.empty();
        }

        return Optional.of(testScope.get(testCase).findElement(name));
    }

    public Optional<Variable> findSuiteVariable(String suite, String name) {
        if(!suiteScope.containsKey(suite)){
            return Optional.empty();
        }

        return Optional.of(suiteScope.get(suite).findElement(name));
    }

    public Variable findGlobalVariable(String name) {
        return globalScope.findElement(name);
    }

    public Optional<Variable> findInScope(List<TestCase> testCases, List<String> suites, String name){
        for(TestCase testCase: testCases){
            Optional variable = findTestVariable(testCase, name);
            if(variable.isPresent()){
                return variable;
            }
        }

        for(String suite: suites){
            Optional variable = findSuiteVariable(suite, name);
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
        suiteScope.putIfAbsent(suite, new ElementTable<>());
        suiteScope.get(suite).add(variable);
    }

    public void setTestScope(TestCase testCase, Variable variable) {
        testScope.putIfAbsent(testCase, new ElementTable<>());
        testScope.get(testCase).add(variable);
    }
}
