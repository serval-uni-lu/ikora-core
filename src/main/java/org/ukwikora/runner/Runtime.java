package org.ukwikora.runner;

import org.ukwikora.model.*;

import java.util.Collections;
import java.util.List;
import java.util.Set;

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

    public abstract Keyword findKeyword(String library, String name) throws InstantiationException, IllegalAccessException;
}
