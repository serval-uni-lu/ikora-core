package org.ukwikora.model;

import java.util.List;

public abstract class Runtime {
    private Scope scope;
    private LibraryResources libraries;
    private Project project;

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

    public Variable findTestVariable(TestCase test, String name) {
        return this.scope.findTestVariable(test, name);
    }

    public Variable findGlobalVariable(String name) {
        return this.scope.findGlobalVariable(name);
    }

    public List<TestCaseFile> getTestCaseFiles(){
        return project.getTestCaseFiles();
    }
}
