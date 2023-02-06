/*
 *
 *     Copyright © 2022 University of Luxembourg
 *
 *     Licensed under the Apache License, Version 2.0 (the "License")
 *     you may not use this file except in compliance with the License.
 *     You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *     Unless required by applicable law or agreed to in writing, software
 *     distributed under the License is distributed on an "AS IS" BASIS,
 *     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *     See the License for the specific language governing permissions and
 *     limitations under the License.
 */
package lu.uni.serval.ikora.core.runner;

import lu.uni.serval.ikora.core.error.ErrorManager;
import lu.uni.serval.ikora.core.exception.BadElementException;
import lu.uni.serval.ikora.core.model.*;
import lu.uni.serval.ikora.core.runner.exception.InternalException;
import lu.uni.serval.ikora.core.runner.exception.RunnerException;
import lu.uni.serval.ikora.core.runner.report.*;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class Runtime {
    private final DynamicScope scope;
    private final ReportBuilder reportBuilder;
    private final ErrorManager errors;

    private final ExecutionFilter filter;

    private LibraryResources libraryResources;

    public Runtime(DynamicScope scope, ErrorManager errorManager, ExecutionFilter filter){
        this.scope = scope;
        this.errors = errorManager;
        this.reportBuilder = new ReportBuilder(errorManager);
        this.filter = filter;
    }

    public void setLibraryResources(LibraryResources libraryResources) {
        this.libraryResources = libraryResources;
    }

    public void addArgumentToScope(List<Resolved> arguments) throws InternalException {
        this.scope.addToArguments(arguments);
    }

    public List<Resolved> getArguments() {
        return this.scope.getArguments();
    }

    public void addToGlobalScope(VariableAssignment variable){
        this.scope.addToGlobalScope(variable);
    }

    public void addToSuiteScope(VariableAssignment variable) throws InternalException {
        this.scope.addToSuiteScope(variable);
    }

    public void addToTestScope(VariableAssignment variable) throws InternalException {
        this.scope.addToTestScope(variable);
    }

    public void addToKeywordScope(VariableAssignment variableAssignment) {
        this.scope.addToKeyword(variableAssignment);
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
        this.scope.exitSuite();
        this.reportBuilder.exitSuite(suite);
    }

    public void exitNode(Node node){
        this.scope.exitNode(node);
        this.reportBuilder.exitNode(node);
    }

    public void finish() {
        this.reportBuilder.finish();
    }

    public Optional<Report> getReport(){
        return reportBuilder.getReport();
    }

    public Optional<TestCase> getTestCase() {
        return scope.getTestCase();
    }

    public List<Value> getReturnValues() {
        return scope.getReturnValues();
    }

    public void registerIOErrorAndThrow(Source source, String message) throws RunnerException {
        this.errors.registerIOError(source, message);
        throw new InternalException("IO error encountered");
    }

    public void registerSymbolErrorAndThrow(Source source, String message, Range range) throws RunnerException {
        this.errors.registerSymbolError(source, message, range);
        throw new InternalException("Symbol error encountered");
    }

    public void registerInternalErrorAndThrow(Source source, String message, Range range) throws RunnerException {
        this.errors.registerInternalError(source, message, range);
        throw new InternalException("Internal error encountered");
    }

    public void registerUnhandledErrorAndThrow(Source source, String message, Exception exception) throws RunnerException {
        this.errors.registerUnhandledError(source, message, exception);
        throw new InternalException("Unhandled Error encountered");
    }

    public ErrorManager getErrors() {
        return errors;
    }

    public DynamicScope getScope() {
        return scope;
    }

    public LibraryResources getLibraryResources(){
        return libraryResources;
    }

    public boolean isInError() {
        return !this.errors.isEmpty();
    }

    public void setMessage(LogLevel level, String message) {
        final ReportElement currentElement = this.reportBuilder.getCurrentElement();

        if(currentElement instanceof KeywordNode keywordNode){
            final MessageNode messageNode = new MessageNode();
            messageNode.setTimestamp(Instant.now());
            messageNode.setLevel(level);
            messageNode.setText(message);

            keywordNode.setMessage(messageNode);
        }
    }

    public Set<Node> find(Variable variable) {
        return scope.findInScope(variable);
    }

    public void setReturnValues(List<Value> values) {
        scope.setReturnValues(values);
    }

    public List<TestCase> filter(List<TestCase> testCases) {
        return this.filter.filter(testCases);
    }
}
