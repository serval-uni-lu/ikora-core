package org.ikora.model;

import org.ikora.exception.InvalidTypeException;
import org.ikora.utils.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Settings implements Delayable {
    private List<Resources> resourcesTable;
    private List<Resources> externalResourcesTable;
    private List<Library> libraryTable;

    private List<String> defaultTags;
    private List<String> forceTags;

    private String documentation;

    private SourceFile file;
    private TimeOut timeOut;
    private Metadata metadata;
    private List<VariableFile> variableFiles;

    private KeywordCall template;
    private KeywordCall testSetup;
    private KeywordCall testTeardown;
    private KeywordCall suiteSetup;
    private KeywordCall suiteTeardown;

    public Settings() {
        this.resourcesTable = new ArrayList<>();
        this.externalResourcesTable = new ArrayList<>();
        this.libraryTable = new ArrayList<>();
        this.defaultTags = new ArrayList<>();
        this.timeOut = TimeOut.none();
        this.metadata = new Metadata();
        this.variableFiles = new ArrayList<>();
    }

    public SourceFile getFile() {
        return file;
    }

    public String getDocumentation() {
        return documentation;
    }

    public List<Resources> getResources() {
        return resourcesTable;
    }

    public List<Resources> getExternalResources() {
        return externalResourcesTable;
    }

    public List<Resources> getAllResources() {
        return Stream.concat(resourcesTable.stream(), externalResourcesTable.stream())
                .collect(Collectors.toList());
    }

    public List<Library> getLibraries() {
        return libraryTable;
    }

    public List<String> getDefaultTags(){
        return defaultTags;
    }

    public KeywordCall getTemplate() {
        return template;
    }

    public KeywordCall getTestSetup() {
        return testSetup;
    }

    public KeywordCall getTestTeardown() {
        return testTeardown;
    }

    public KeywordCall getSuiteSetup() {
        return suiteSetup;
    }

    public KeywordCall getSuiteTeardown() {
        return suiteTeardown;
    }

    public void setFile(SourceFile file) {
        this.file = file;
    }

    public void setDocumentation(String documentation){
        this.documentation = documentation;
    }

    public void addResources(Resources resources) throws IOException {
        if(this.file == null){
            resourcesTable.add(resources);
        }
        else{
            Project project = this.file.getProject();
            File rootFolder = project.getRootFolder();

            if(FileUtils.isSubDirectory(rootFolder, resources.getFile())){
                resourcesTable.add(resources);
            }
            else{
                externalResourcesTable.add(resources);
            }
        }
    }

    public void addLibrary(Library library) {
        this.libraryTable.add(library);
    }

    public void addDefaultTag(String defaultTag){
        this.defaultTags.add(defaultTag);
    }

    public void addForceTag(String forceTag){
        this.forceTags.add(forceTag);
    }

    @Override
    public TimeOut getTimeOut() {
        return timeOut;
    }

    @Override
    public void setTimeOut(TimeOut timeOut) {
        this.timeOut = timeOut;
    }

    public void addMetadata(String key, Token token){
        if(key.isEmpty()){
            return;
        }

        metadata.addEntry(key, token);
    }

    public void addVariableFile(VariableFile variableFile){
        if(variableFile == null){
            return;
        }

        variableFiles.add(variableFile);
    }

    public void setTemplate(Step template) throws InvalidTypeException {
        setTemplate(template.toCall());
    }

    public void setTemplate(KeywordCall template) {
        this.template = template;
    }

    public void setTestSetup(Step testSetup) throws InvalidTypeException {
        setTestSetup(testSetup.toCall());
    }

    public void setTestSetup(KeywordCall testSetup) {
        this.testSetup = testSetup;
    }

    public void setTestTeardown(Step testTeardown) throws InvalidTypeException {
        setTestTeardown(testTeardown.toCall());
    }

    public void setTestTeardown(KeywordCall testTeardown) {
        this.testTeardown = testTeardown;
    }

    public void setSuiteSetup(Step suiteSetup) throws InvalidTypeException {
        setSuiteSetup(suiteSetup.toCall());
    }

    public void setSuiteSetup(KeywordCall suiteSetup) {
        this.suiteSetup = suiteSetup;
    }

    public void setSuiteTeardown(Step suiteTeardown) throws InvalidTypeException {
        setSuiteTeardown(suiteTeardown.toCall());
    }

    public void setSuiteTeardown(KeywordCall suiteTeardown) {
        this.suiteTeardown = suiteTeardown;
    }
}
