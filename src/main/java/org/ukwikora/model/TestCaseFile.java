package org.ukwikora.model;

import org.apache.commons.io.FilenameUtils;
import org.ukwikora.compiler.Line;

import javax.annotation.Nonnull;
import java.io.File;
import java.util.*;

public class TestCaseFile implements Iterable<UserKeyword> {
    final private Project project;
    final private File file;

    private int loc;
    private List<Line> lines;

    private String name;
    private Settings settings;
    private StatementTable<TestCase> testCaseTable;
    private StatementTable<UserKeyword> userKeywordTable;
    private StatementTable<Variable> variableTable;

    public TestCaseFile(Project project, File file){
        this.project = project;
        this.file = file;
        this.loc = 0;
        this.lines = new ArrayList<>();

        setSettings(new Settings());
        setTestCaseTable(new StatementTable<>());
        setKeywordTable(new StatementTable<>());
        setVariableTable(new StatementTable<>());
    }

    public void setName(String name) {
        this.name = name;

        this.settings.setFile(this);
        this.testCaseTable.setFile(this);
        this.userKeywordTable.setFile(this);
        this.variableTable.setFile(this);
    }

    public void setLoc(int loc) {
        this.loc = loc;
    }

    public int getLoc(){
        return this.loc;
    }

    public int getLoc(LineRange lineRange) {
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

    public void setTestCaseTable(StatementTable<TestCase> testCaseTable) {
        this.testCaseTable = testCaseTable;
        this.testCaseTable.setFile(this);
    }

    public void setKeywordTable(StatementTable<UserKeyword> statementTable) {
        this.userKeywordTable = statementTable;
        this.userKeywordTable.setFile(this);
    }

    public void setVariableTable(StatementTable<Variable> variableTable) {
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

    TestCase getTestCase(String name) {
        return testCaseTable.findStatement(name);
    }

    @Nonnull
    public Iterator<UserKeyword> iterator() {
        return userKeywordTable.iterator();
    }

    public UserKeyword findUserKeyword(String name) {
        return (UserKeyword)findStatement(null, name, new HashSet<>(), UserKeyword.class);
    }

    public UserKeyword findUserKeyword(String library, String name) {
        return (UserKeyword)findStatement(library, name, new HashSet<>(), UserKeyword.class);
    }

    public Variable findVariable(String name) {
        return (Variable) findStatement(null, name, new HashSet<>(), Variable.class);
    }

    public Variable findVariable(String library, String name) {
        return (Variable) findStatement(library, name, new HashSet<>(), Variable.class);
    }

    private <T> Statement findStatement(String library, String name, Set<TestCaseFile> memory, Class<T> type){
        Statement statement = null;

        if(library == null || library.isEmpty() || getLibraryName().equalsIgnoreCase(library)){
            if(type == UserKeyword.class){
                statement = userKeywordTable.findStatement(name);
            }

            if(statement == null &&type == TestCase.class){
                statement = testCaseTable.findStatement(name);
            }

            if(statement == null && type == Variable.class){
                statement = variableTable.findStatement(name);
            }

            if(statement != null){
                return statement;
            }
        }

        for(Resources resources: settings.getAllResources()){
            if(memory.add(resources.getTestCaseFile())) {
                statement = resources.getTestCaseFile().findStatement(library, name, memory, type);
            }

            if(statement != null){
                return statement;
            }
        }

        return null;
    }


    public void addLine(Line line) {
        lines.add(line);
    }
}
