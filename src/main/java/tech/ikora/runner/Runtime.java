package tech.ikora.runner;

import tech.ikora.error.ErrorManager;
import tech.ikora.report.Report;
import tech.ikora.report.ReportBuilder;
import tech.ikora.model.*;

import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class Runtime {
    private final Scope scope;
    private final Project project;
    private final ReportBuilder reportBuilder;
    private final ErrorManager errors;

    private LibraryResources libraries;

    public Runtime(Project project, Scope scope, ErrorManager errors){
        this.project = project;
        this.scope = scope;
        this.reportBuilder = new ReportBuilder();
        this.errors = errors;
    }

    public void setLibraries(LibraryResources libraries) {
        this.libraries = libraries;
    }

    public Set<LibraryVariable> findLibraryVariable(Token name){
        return Collections.singleton(this.libraries.findVariable(name));
    }

    public List<SourceFile> getSourceFiles(){
        return project.getSourceFiles();
    }

    public Set<Node> findInScope(Set<TestCase> testCases, Set<String> suites, Token name){
        return scope.findInScope(testCases, suites, name);
    }

    public Keyword findKeyword(String library, Token name) throws InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        if(library == null){
            return null;
        }

        return libraries.findKeyword(library, name);
    }

    public Set<Variable> findVariableAssigment(Variable variable) {
        //TODO: define a strategy to find the variable assignment
        return Collections.emptySet();
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

    public void addToKeywordScope(Keyword keyword, Variable variable) {
        this.scope.addToKeyword(keyword, variable);
    }

    public void addDynamicLibrary(KeywordDefinition keyword, List<Argument> argumentList){
        this.scope.addDynamicLibrary(keyword, argumentList);
    }

    public void reset(){
        this.scope.reset();
        this.reportBuilder.reset();
    }

    public void enterSuite(Suite suite) throws Exception {
        this.scope.enterSuite(suite);
        this.reportBuilder.enterSuite(suite);
    }

    public void enterNode(Node node) throws Exception {
        this.scope.enterNode(node);
        this.reportBuilder.enterNode(node);
    }

    public void exitSuite(Suite suite) {
        this.scope.exitSuite(suite);
        this.reportBuilder.exitSuite(suite);
    }

    public void exitNode(Node node){
        this.scope.exitNode(node);
        this.reportBuilder.exitNode(node);
    }

    public void finish() {
        this.reportBuilder.finish();
    }

    Optional<Report> getReport(){
        return reportBuilder.getReport();
    }

    public KeywordDefinition getTestCase() {
        return scope.getTestCase();
    }

    public List<Variable> getReturnVariables() {
        return scope.getReturnVariables();
    }

    public ErrorManager getErrors() {
        return errors;
    }
}
