package org.ikora.model;

import org.apache.commons.io.FilenameUtils;
import org.ikora.builder.Line;

import javax.annotation.Nonnull;
import java.io.File;
import java.util.*;

public class SourceFile implements Iterable<UserKeyword> {
    final private Project project;
    final private File file;

    private int linesOfCode;
    private int commentLines;
    private List<Line> lines;

    private String name;
    private Settings settings;
    private NodeTable<TestCase> testCaseTable;
    private NodeTable<UserKeyword> userKeywordTable;
    private NodeTable<Variable> variableTable;

    public SourceFile(Project project, File file){
        this.project = project;
        this.file = file;
        this.linesOfCode = 0;
        this.commentLines = 0;
        this.lines = new ArrayList<>();

        setSettings(new Settings());
        setTestCaseTable(new NodeTable<>());
        setKeywordTable(new NodeTable<>());
        setVariableTable(new NodeTable<>());
    }

    public void setName(String name) {
        this.name = name;

        this.settings.setFile(this);
        this.testCaseTable.setFile(this);
        this.userKeywordTable.setFile(this);
        this.variableTable.setFile(this);
    }

    public void setLinesOfCode(int linesOfCode) {
        this.linesOfCode = linesOfCode;
    }

    public void setCommentLines(int commentLines) {
        this.commentLines = commentLines;
    }

    public int getLinesOfCode(){
        return this.linesOfCode;
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

    public int getCommentLines(){
        return commentLines;
    }

    public int getLinesOfCode(LineRange lineRange) {
        int loc = 0;

        for(int index = lineRange.getStart(); index < lineRange.getEnd(); ++index){
            loc += lines.get(index).ignore() ? 0 : 1;
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

    Set<TestCase> getTestCase(String name) {
        return testCaseTable.findNode(name);
    }

    @Nonnull
    public Iterator<UserKeyword> iterator() {
        return userKeywordTable.iterator();
    }

    public Set<UserKeyword> findUserKeyword(String name) {
        return findNode(null, name, new HashSet<>(), UserKeyword.class);
    }

    public Set<UserKeyword> findUserKeyword(String library, String name) {
        return findNode(library, name, new HashSet<>(), UserKeyword.class);
    }

    public Set<Variable> findVariable(String name) {
        return findNode(null, name, new HashSet<>(), Variable.class);
    }

    public Set<Variable> findVariable(String library, String name) {
        return findNode(library, name, new HashSet<>(), Variable.class);
    }

    private <T> Set<T> findNode(String library, String name, Set<SourceFile> memory, Class<T> type){
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
