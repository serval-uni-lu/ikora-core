package lu.uni.serval.ikora.core.model;

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

import lu.uni.serval.ikora.core.analytics.difference.Edit;
import lu.uni.serval.ikora.core.analytics.visitor.NodeVisitor;
import lu.uni.serval.ikora.core.analytics.visitor.VisitorMemory;
import lu.uni.serval.ikora.core.exception.RunnerException;
import lu.uni.serval.ikora.core.runner.Runtime;
import lu.uni.serval.ikora.core.utils.FileUtils;
import org.apache.commons.lang3.NotImplementedException;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

public class Settings extends SourceNode implements Delayable {
    private Token header;

    private final NodeList<Resources> resourcesTable;
    private final NodeList<Library> libraryTable;
    private final NodeList<VariableFile> variableFiles;

    private final NodeList<Metadata> metadataList;

    private NodeList<Literal> defaultTags;
    private NodeList<Literal> forceTags;

    private Documentation documentation;
    private TimeOut timeOut;

    private TestProcessing testTemplate;
    private TestProcessing testSetup;
    private TestProcessing testTeardown;
    private TestProcessing suiteSetup;
    private TestProcessing suiteTeardown;

    public Settings() {
        this.header = Token.empty();

        this.resourcesTable = new NodeList<>();
        addAstChild(this.resourcesTable);

        this.libraryTable = new NodeList<>();
        addAstChild(this.libraryTable);

        this.defaultTags = new NodeList<>();
        addAstChild(this.defaultTags);

        this.forceTags = new NodeList<>();
        addAstChild(this.forceTags);

        this.timeOut = TimeOut.none();
        addAstChild(this.timeOut);

        this.metadataList = new NodeList<>();
        addAstChild(this.metadataList);

        this.variableFiles = new NodeList<>();
        addAstChild(this.variableFiles);
    }

    @Override
    public Token getDefinitionToken() {
        return this.header;
    }

    public Token getHeader() {
        return header;
    }

    public Documentation getDocumentation() {
        return documentation;
    }

    public List<Resources> getInternalResources() {
        Project project = getProject();

        if(project == null){
            return Collections.emptyList();
        }

        File rootFolder = project.getRootFolder().asFile();

        return this.resourcesTable.stream()
                .filter(r -> FileUtils.isSubDirectory(rootFolder, r.getFile()))
                .collect(Collectors.toList());
    }

    public List<Resources> getExternalResources() {
        Project project = getProject();

        if(project == null){
            return Collections.emptyList();
        }

        File rootFolder = project.getRootFolder().asFile();

        return this.resourcesTable.stream()
                .filter(r -> !FileUtils.isSubDirectory(rootFolder, r.getFile()))
                .collect(Collectors.toList());
    }

    public List<Resources> getResources() {
        return resourcesTable;
    }

    public List<Library> getLibraries() {
        return libraryTable;
    }

    public boolean containsLibrary(String libraryName){
        if(libraryName.equalsIgnoreCase(Library.BUILTIN)){
            return true;
        }

        return libraryTable.stream()
                .map(Library::getDefinitionToken)
                .anyMatch(n -> n.matches(libraryName));
    }

    public boolean hasDefaultTag(String tag){
        return defaultTags.stream().anyMatch(literal -> literal.matches(Token.fromString(tag)));
    }

    public boolean hasForceTag(String tag){
        return forceTags.stream().anyMatch(literal -> literal.matches(Token.fromString(tag)));
    }

    public TestProcessing getTestTemplate() {
        return testTemplate;
    }

    public TestProcessing getTestSetup() {
        return testSetup;
    }

    public TestProcessing getTestTeardown() {
        return testTeardown;
    }

    public TestProcessing getSuiteSetup() {
        return suiteSetup;
    }

    public TestProcessing getSuiteTeardown() {
        return suiteTeardown;
    }

    public List<Metadata> getMetadataList(){
        return this.metadataList;
    }

    public Optional<Value> getMetadata(String name){
        return metadataList.stream()
                .filter(m -> m.getName().matches(name))
                .findFirst()
                .map(Metadata::getValue);
    }

    public List<VariableFile> getVariableFiles() {
        return variableFiles;
    }

    public Optional<VariableFile> getVariableFile(String name){
        return variableFiles.stream().filter(file -> file.getName().equalsIgnoreCase(name)).findAny();
    }

    public void setHeader(Token header){
        this.header = header;
    }

    public void setDocumentation(Documentation documentation){
        this.documentation = documentation;
    }

    public void addResources(Resources resources) {
        resourcesTable.add(resources);
    }

    public void addLibrary(Library library) {
        this.libraryTable.add(library);
    }

    public void setDefaultTags(NodeList<Literal> defaultTags){
        this.defaultTags = defaultTags;
    }

    public void setForceTags(NodeList<Literal> forceTags){
        this.forceTags = forceTags;
    }

    @Override
    public TimeOut getTimeOut() {
        return timeOut;
    }

    @Override
    public void setTimeOut(TimeOut timeOut) {
        this.timeOut = timeOut;
    }

    public void addMetadata(Metadata metadata) {
        metadataList.add(metadata);
    }

    public void addVariableFile(VariableFile variableFile){
        if(variableFile == null){
            return;
        }

        variableFiles.add(variableFile);
    }

    public void setTestTemplate(TestProcessing testTemplate) {
        this.testTemplate = testTemplate;
        addAstChild(this.testTemplate);
    }

    public void setTestSetup(TestProcessing testSetup) {
        this.testSetup = testSetup;
        addAstChild(this.testSetup);
    }

    public void setTestTeardown(TestProcessing testTeardown) {
        this.testTeardown = testTeardown;
        addAstChild(this.testTeardown);
    }

    public void setSuiteSetup(TestProcessing suiteSetup) {
        this.suiteSetup = suiteSetup;
        addAstChild(this.suiteSetup);
    }

    public void setSuiteTeardown(TestProcessing suiteTeardown) {
        this.suiteTeardown = suiteTeardown;
        addAstChild(this.suiteTeardown);
    }

    @Override
    public List<Edit> differences(SourceNode other) {
        if(other == null){
            return Collections.singletonList(Edit.removeElement(this.getClass(), this));
        }

        if(other == this){
            return Collections.emptyList();
        }

        return Collections.emptyList();
    }

    @Override
    public boolean matches(Token name) {
        return this.header.matches(name);
    }

    @Override
    public void accept(NodeVisitor visitor, VisitorMemory memory) {
        visitor.visit(this, memory);
    }

    @Override
    public void execute(Runtime runtime) throws RunnerException {
        throw new NotImplementedException("Execution is not implemented yet");
    }
}
