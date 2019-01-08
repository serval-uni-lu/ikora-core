package org.ukwikora.model;

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
    private ElementTable<TestCase> testCaseTable;
    private ElementTable<UserKeyword> userKeywordTable;
    private ElementTable<Variable> variableTable;

    private ElementTable<UserKeyword> userKeywordCache;
    private ElementTable<UserKeyword> externalKeywordCache;
    private ElementTable<Variable> variableCache;

    public TestCaseFile(Project project, File file){
        this.project = project;
        this.file = file;
        this.loc = 0;
        this.lines = new ArrayList<>();

        setSettings(new Settings());
        setTestCaseTable(new ElementTable<>());
        setKeywordTable(new ElementTable<>());
        setVariableTable(new ElementTable<>());
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

    public void setTestCaseTable(ElementTable<TestCase> testCaseTable) {
        this.testCaseTable = testCaseTable;
        this.testCaseTable.setFile(this);
    }

    public void setKeywordTable(ElementTable<UserKeyword> elementTable) {
        this.userKeywordTable = elementTable;
        this.userKeywordTable.setFile(this);
        this.userKeywordCache = null;
    }

    public void setVariableTable(ElementTable<Variable> variableTable) {
        this.variableTable = variableTable;
        this.variableTable.setFile(this);
        this.variableCache = null;
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

    public Settings getSettings() {
        return this.settings;
    }

    public List<TestCase> getTestCases(){
        return testCaseTable.asList();
    }

    public <T extends Element> ElementTable<T> getElements(Class<T> type) {
        Set<TestCaseFile> files = new HashSet<>();
        return getElements(type, files);
    }

    private <T extends Element> ElementTable<T> getElements(Class<T> type, Set<TestCaseFile> files) {
        ElementTable<T> keywords = new ElementTable<>();

        if(type == UserKeyword.class){
            keywords.extend((ElementTable<T>) userKeywordTable);
        }
        else if(type == TestCase.class){
            keywords.extend((ElementTable<T>) testCaseTable);
        }
        else if(type == Variable.class){
            keywords.extend((ElementTable<T>) variableTable);
        }

        files.add(this);

        for(Resources resources: settings.getResources()){
            if(files.contains(resources.getTestCaseFile())) {
                keywords.extend(resources.getTestCaseFile().getElements(type, files));
            }
        }

        return keywords;
    }

    public <T extends Element> ElementTable<T> getExternalElements(Class<T> type) {
        ElementTable<T> keywords = new ElementTable<>();

        for(Resources resources: settings.getExternalResources()){
            keywords.extend(resources.getTestCaseFile().getElements(type));
        }

        return keywords;
    }

    public long getEpoch() {
        return this.project.getEpoch();
    }

    TestCase getTestCase(String name) {
        return testCaseTable.findElement(name);
    }

    @Nonnull
    public Iterator<UserKeyword> iterator() {
        return userKeywordTable.iterator();
    }

    public KeywordDefinition findUserKeyword(String name) {
        if(userKeywordCache == null){
            userKeywordCache = getElements(UserKeyword.class);
        }

        KeywordDefinition userKeyword = userKeywordCache.findElement(name);

        if(userKeyword == null) {
            if(externalKeywordCache == null) {
                externalKeywordCache = getExternalElements(UserKeyword.class);
            }
        }

        return userKeyword;
    }

    public Variable findVariable(String name) {
        if(variableCache == null){
            variableCache = getElements(Variable.class);
        }

        return variableCache.findElement(name);
    }

    public void addLine(Line line) {
        lines.add(line);
    }
}
