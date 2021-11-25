package lu.uni.serval.ikora.core.runner;

/*-
 * #%L
 * Ikora Core
 * %%
 * Copyright (C) 2019 - 2021 University of Luxembourg
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License")
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import lu.uni.serval.ikora.core.error.ErrorManager;
import lu.uni.serval.ikora.core.exception.BadElementException;
import lu.uni.serval.ikora.core.exception.RunnerException;
import lu.uni.serval.ikora.core.model.*;
import lu.uni.serval.ikora.core.report.Report;
import lu.uni.serval.ikora.core.report.ReportBuilder;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public class Runtime {
    private final Scope scope;
    private final Project project;
    private final ReportBuilder reportBuilder;
    private final ErrorManager errors;

    private LibraryResources libraryResources;

    public Runtime(Project project, Scope scope, ErrorManager errors){
        this.project = project;
        this.scope = scope;
        this.reportBuilder = new ReportBuilder();
        this.errors = errors;
    }

    public void setLibraryResources(LibraryResources libraryResources) {
        this.libraryResources = libraryResources;
    }

    public List<SourceFile> getSourceFiles(){
        return project.getSourceFiles();
    }

    public Set<Node> findInScope(Set<TestCase> testCases, Set<String> suites, Token name){
        return scope.findInScope(testCases, suites, name);
    }

    public Optional<Keyword> findLibraryKeyword(Set<Library> libraries, Token name) {
        final Optional<Keyword> libraryKeyword = libraries.stream()
                .map(Library::getName)
                .map(library -> libraryResources.findKeyword(library, name))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .findFirst();

        return libraryKeyword.isPresent() ? libraryKeyword : libraryResources.findKeyword("", name);
    }

    public Optional<LibraryVariable> findLibraryVariable(String library, Token name) {
        return libraryResources.findVariable(library, name);
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

    public void enterSuite(Suite suite) throws RunnerException {
        try{
            this.scope.enterSuite(suite);
            this.reportBuilder.enterSuite(suite);
        } catch (BadElementException e){
            registerUnhandledErrorAndThrow(null, "Failed to register suite to report", e);
        }
    }

    public void enterNode(Node node) throws RunnerException {
        try{
            this.reportBuilder.enterNode(node);
            this.scope.enterNode(node);
        } catch (BadElementException e){
            registerUnhandledErrorAndThrow(null, "Failed to register node to report", e);
        }
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

    public NodeList<Value> getReturnValues() {
        return scope.getReturnValues();
    }

    public void registerIOErrorAndThrow(Source source, String message) throws RunnerException {
        this.errors.registerIOError(source, message);
        throw new RunnerException("IO error encountered");
    }

    public void registerSymbolErrorAndThrow(Source source, String message, Range range) throws RunnerException {
        this.errors.registerSymbolError(source, message, range);
        throw new RunnerException("Symbol error encountered");
    }

    public void registerInternalErrorAndThrow(Source source, String message, Range range) throws RunnerException {
        this.errors.registerInternalError(source, message, range);
        throw new RunnerException("Internal error encountered");
    }

    public void registerUnhandledErrorAndThrow(Source source, String message, Exception exception) throws RunnerException {
        this.errors.registerUnhandledError(source, message, exception);
        throw new RunnerException("Unhandled Error encountered");
    }

    public ErrorManager getErrors() {
        return errors;
    }
}
