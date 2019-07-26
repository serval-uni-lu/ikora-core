package org.ukwikora.runner;

import org.ukwikora.model.*;
import org.ukwikora.report.Report;
import org.ukwikora.report.ReportBuilder;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class Runtime {
    private Scope scope;
    private LibraryResources libraries;
    private Project project;
    private ReportBuilder reportBuilder;

    public Runtime(Project project, Scope scope){
        this.project = project;
        this.scope = scope;
        this.reportBuilder = new ReportBuilder();
    }

    public void setLibraries(LibraryResources libraries) {
        this.libraries = libraries;
    }

    public Set<Variable> findLibraryVariable(String name){
        return Collections.singleton(this.libraries.findVariable(name));
    }

    public List<TestCaseFile> getTestCaseFiles(){
        return project.getTestCaseFiles();
    }

    public Set<Variable> findInScope(Set<TestCase> testCases, Set<String> suites, String name){
        return scope.findInScope(testCases, suites, name);
    }

    public Keyword findKeyword(String library, String name) throws InstantiationException, IllegalAccessException{
        if(library == null){
            return null;
        }

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

    public void reset(){
        this.scope.reset();
        this.reportBuilder.reset();
    }

    public void enterKeyword(Keyword keyword) throws Exception {
        this.scope.enterKeyword(keyword);
        this.reportBuilder.enterKeyword(keyword);
    }

    public void exitKeyword(Keyword keyword){
        this.scope.exitKeyword(keyword);
        this.reportBuilder.exitKeyword(keyword);
    }

    public void finish() {
    }

    Optional<Report> getReport(){
        return reportBuilder.getReport();
    }

    public KeywordDefinition getTestCase() {
        return scope.getTestCase();
    }
}
