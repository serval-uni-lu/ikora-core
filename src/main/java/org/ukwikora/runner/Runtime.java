package org.ukwikora.runner;

import org.ukwikora.model.*;
import org.ukwikora.report.Report;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class Runtime {
    protected Scope scope;
    protected LibraryResources libraries;
    protected Project project;
    protected Report report;

    public Runtime(Project project){
        this.project = project;
        this.scope = new Scope();
        this.report = null;
    }

    public void setLibraries(LibraryResources libraries) {
        this.libraries = libraries;
    }

    public Set<Variable> findLibraryVariable(String name){
        return Collections.singleton(this.libraries.findVariable(name));
    }

    public Set<Variable> findTestVariable(TestCase test, String name) {
        return this.scope.findTestVariable(test, name);
    }

    public Set<Variable> findSuiteVariable(String suite, String name) {
        return this.scope.findSuiteVariable(suite, name);
    }

    public Set<Variable> findGlobalVariable(String name) {
        return this.scope.findGlobalVariable(name);
    }

    public List<TestCaseFile> getTestCaseFiles(){
        return project.getTestCaseFiles();
    }

    public Set<Variable> findInScope(Set<TestCase> testCases, Set<String> suites, String name){
        return scope.findInScope(testCases, suites, name);
    }

    public Keyword findKeyword(String library, String name) throws InstantiationException, IllegalAccessException{
        return libraries.findKeyword(library, name);
    }

    public void addToGlobalScope(Variable variable){
        this.scope.addToGlobal(variable);
    }

    public void addToSuiteScope(String suite, Variable variable){
        this.scope.addToSuite(suite, variable);
    }

    public void addToTestScope(TestCase testCase, Variable variable){
        this.scope.addToTest(testCase, variable);
    }

    public void addDynamicLibrary(KeywordDefinition keyword, List<Value> parameters){
        this.scope.addDynamicLibrary(keyword, parameters);
    }

    public void enterKeyword(Keyword keyword){

    }

    public void exitKeyword(Keyword step){

    }

    Optional<Report> getReport(){
        return Optional.ofNullable(report);
    }
}
