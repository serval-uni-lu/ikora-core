package org.ukwikora.model;

import java.util.List;
import java.util.Optional;

public abstract class Runtime {
    protected Scope scope;
    protected LibraryResources libraries;
    protected Project project;

    public Runtime(Project project){
        this.project = project;
        this.scope = new Scope();
    }

    public void setLibraries(LibraryResources libraries) {
        this.libraries = libraries;
    }

    public LibraryResources getLibraries() {
        return libraries;
    }

    public Variable findLibraryVariable(String name){
        return this.libraries.findVariable(name);
    }

    public Optional<Variable> findTestVariable(TestCase test, String name) {
        return this.scope.findTestVariable(test, name);
    }

    public Optional<Variable> findSuiteVariable(String suite, String name) {
        return this.scope.findSuiteVariable(suite, name);
    }

    public Variable findGlobalVariable(String name) {
        return this.scope.findGlobalVariable(name);
    }

    public List<TestCaseFile> getTestCaseFiles(){
        return project.getTestCaseFiles();
    }
}
