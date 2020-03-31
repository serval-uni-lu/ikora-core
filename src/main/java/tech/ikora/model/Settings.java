package tech.ikora.model;

import tech.ikora.exception.InvalidMetadataException;
import tech.ikora.exception.InvalidTypeException;
import tech.ikora.utils.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Settings implements Delayable {
    private Token header;

    private List<Resources> resourcesTable;
    private List<Resources> externalResourcesTable;
    private List<Library> libraryTable;

    private Set<Token> defaultTags;
    private Set<Token> forceTags;

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
        this.header = Token.empty();

        this.resourcesTable = new ArrayList<>();
        this.externalResourcesTable = new ArrayList<>();
        this.libraryTable = new ArrayList<>();
        this.defaultTags = new HashSet<>();
        this.forceTags = new HashSet<>();
        this.timeOut = TimeOut.none();
        this.metadata = new Metadata();
        this.variableFiles = new ArrayList<>();
    }

    public Token getHeader() {
        return header;
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

    public boolean containsLibrary(String libraryName){
        return libraryTable.stream()
                .map(l -> l.getName().getText())
                .anyMatch(n -> n.equalsIgnoreCase(libraryName));
    }

    public Set<Token> getDefaultTags(){
        return defaultTags;
    }

    public boolean hasDefaultTag(String tag){
        return defaultTags.stream().anyMatch(token -> token.getText().equalsIgnoreCase(tag));
    }

    public Set<Token> getForceTags(){
        return forceTags;
    }

    public boolean hasForceTag(String tag){
        return forceTags.stream().anyMatch(token -> token.getText().equalsIgnoreCase(tag));
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

    public Metadata getMetadata(){
        return metadata;
    }

    public Token getMetadata(String key){
        return metadata.get(key);
    }

    public List<VariableFile> getVariableFiles() {
        return variableFiles;
    }

    public Optional<VariableFile> getVariableFile(String name){
        return variableFiles.stream().filter(file -> file.getPath().equalsIgnoreCase(name)).findAny();
    }

    public void setHeader(Token header){
        this.header = header;
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

    public void addDefaultTag(Token defaultTag){
        this.defaultTags.add(defaultTag);
    }

    public void addForceTag(Token forceTag){
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

    public void addMetadata(Token key, Token value) throws InvalidMetadataException {
        if(key == null || key.isEmpty()){
            throw new InvalidMetadataException("No key defined");
        }

        if(value == null || value.isEmpty()){
            throw new InvalidMetadataException("No value defined");
        }

        metadata.addEntry(key, value);
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
