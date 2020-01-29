package org.ikora.model;

import org.apache.commons.io.FilenameUtils;
import org.ikora.builder.Line;

import java.io.File;
import java.util.*;

public class SourceFile implements Iterable<UserKeyword> {
    final private Project project;
    final private File file;
    final private List<Line> lines;

    private String name;
    private Settings settings;
    private NodeTable<TestCase> testCaseTable;
    private NodeTable<UserKeyword> userKeywordTable;
    private NodeTable<Variable> variableTable;

    public SourceFile(Project project, File file){
        this.project = project;
        this.file = file;
        this.lines = new ArrayList<>();

        setSettings(new Settings());
        setTestCaseTable(new NodeTable<>());
        setKeywordTable(new NodeTable<>());
        setVariableTable(new NodeTable<>());

        String name = this.project.generateFileName(this.file);
        setName(name);
    }

    private void setName(String name) {
        this.name = name;

        this.settings.setFile(this);
        this.testCaseTable.setFile(this);
        this.userKeywordTable.setFile(this);
        this.variableTable.setFile(this);
    }

    public List<Line> getLines(){
        return this.lines;
    }

    public int getDeadLoc() {
        int deadLoc = 0;

        for(UserKeyword keyword: userKeywordTable){
            if(keyword.getDependencies().isEmpty()){
                deadLoc += keyword.getLoc();
            }
        }

        for(Variable variable: variableTable){
            if(variable.getDependencies().isEmpty()){
                deadLoc += variable.getLoc();
            }
        }

        return deadLoc;
    }

    public int getLinesOfCode(){
        return getLinesOfCode(0, lines.size());
    }

    public int getLinesOfCode(int start, int end) {
        int loc = 0;

        for(int index = start; index < end; ++index){
            loc += lines.get(index).isCode() ? 1 : 0;
        }

        return loc;
    }

    public void setSettings(Settings settings) {
        this.settings = settings;
        this.settings.setFile(this);
    }

    public void setTestCaseTable(NodeTable<TestCase> testCaseTable) {
        this.testCaseTable = testCaseTable;
        this.testCaseTable.setFile(this);
    }

    public void setKeywordTable(NodeTable<UserKeyword> nodeTable) {
        this.userKeywordTable = nodeTable;
        this.userKeywordTable.setFile(this);
    }

    public void setVariableTable(NodeTable<Variable> variableTable) {
        this.variableTable = variableTable;
        this.variableTable.setFile(this);
    }

    public Project getProject() {
        return project;
    }

    public File getFile() {
        return file;
    }

    public String getDirectory(){
        return this.file.getParent();
    }

    public String getPath() {
        return this.file.getPath();
    }

    public String getName() {
        return this.name;
    }

    public String getLibraryName() {
        return FilenameUtils.getBaseName(getPath());
    }

    public Settings getSettings() {
        return this.settings;
    }

    public List<TestCase> getTestCases(){
        return testCaseTable.asList();
    }

    public List<UserKeyword> getUserKeywords() {
        return userKeywordTable.asList();
    }

    public List<Variable> getVariables() {
        return variableTable.asList();
    }

    public long getEpoch() {
        return this.project.getEpoch();
    }

    Set<TestCase> getTestCase(Token name) {
        return testCaseTable.findNode(name);
    }

    public Iterator<UserKeyword> iterator() {
        return userKeywordTable.iterator();
    }

    public Set<UserKeyword> findUserKeyword(Token name) {
        return findNode(null, name, new HashSet<>(), UserKeyword.class);
    }

    public Set<UserKeyword> findUserKeyword(String library, Token name) {
        return findNode(library, name, new HashSet<>(), UserKeyword.class);
    }

    public Set<Variable> findVariable(Token name) {
        return findNode(null, name, new HashSet<>(), Variable.class);
    }

    public Set<Variable> findVariable(String library, Token name) {
        return findNode(library, name, new HashSet<>(), Variable.class);
    }

    private <T> Set<T> findNode(String library, Token name, Set<SourceFile> memory, Class<T> type){
        HashSet<T> nodes = new HashSet<>();

        if(library == null || library.isEmpty() || getLibraryName().equalsIgnoreCase(library)){
            if(type == UserKeyword.class){
                nodes.addAll((Collection<? extends T>) userKeywordTable.findNode(name));
            }

            if(type == TestCase.class){
                nodes.addAll((Collection<? extends T>) testCaseTable.findNode(name));
            }

            if(type == Variable.class){
                nodes.addAll((Collection<? extends T>) variableTable.findNode(name));
            }
        }

        for(Resources resources: settings.getAllResources()){
            if(resources.isValid() && memory.add(resources.getSourceFile())) {
                SourceFile file = resources.getSourceFile();
                nodes.addAll(file.findNode(library, name, memory, type));
            }
        }

        return nodes;
    }


    public void addLine(Line line) {
        lines.add(line);
    }

    public boolean isDirectDependency(SourceFile file) {
        if(file == null){
            return false;
        }

        if(this == file){
            return true;
        }

        for(Resources resources: settings.getAllResources()){
            if(resources.getSourceFile() == file){
                return true;
            }
        }

        return false;
    }
}
