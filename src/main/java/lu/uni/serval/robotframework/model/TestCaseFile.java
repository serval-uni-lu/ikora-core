package lu.uni.serval.robotframework.model;

import javax.annotation.Nonnull;
import java.io.File;
import java.util.*;

public class TestCaseFile implements Iterable<UserKeyword> {
    private File file;
    private String name;
    private Settings settings;
    private TestCaseTable testCaseTable;
    private KeywordTable keywordTable;
    private VariableTable variableTable;

    public TestCaseFile(File file){
        this.file = file;

        setSettings(new Settings());
        setTestCaseTable(new TestCaseTable());
        setKeywordTable(new KeywordTable());
        setVariableTable(new VariableTable());
    }

    public void setName(String name) {
        this.name = name;

        this.settings.setFile(this.name);
        this.testCaseTable.setFile(this.name);
        this.keywordTable.setFile(this.name);
        this.variableTable.setFile(this.name);
    }

    public void setSettings(Settings settings) {
        this.settings = settings;
        this.settings.setFile(this.name);
    }

    public void setTestCaseTable(TestCaseTable testCaseTable) {
        this.testCaseTable = testCaseTable;
        this.testCaseTable.setFile(this.name);
    }

    public void setKeywordTable(KeywordTable keywordTable) {
        this.keywordTable = keywordTable;
        this.keywordTable.setFile(this.name);
    }

    public void setVariableTable(VariableTable variableTable) {
        this.variableTable = variableTable;
        this.variableTable.setFile(this.name);
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
        return this.getName();
    }

    public Settings getSettings() {
        return this.settings;
    }

    public List<TestCase> getTestCases(){
        return testCaseTable.getTestCases();
    }

    public KeywordTable getUserKeywords() {
        KeywordTable userKeywords = new KeywordTable(keywordTable);

        for(Resources resources: settings.getResources()){
            userKeywords.extend(resources.getTestCaseFile().getUserKeywords());
        }

        return userKeywords;
    }

    public VariableTable getVariables() {
        VariableTable variables = new VariableTable();
        getVariables(variables);

        return variables;
    }

    private void getVariables(VariableTable variables) {
        for(VariableTable.Entry<String, Variable> variable: variableTable.entrySet()) {
            if(variables.containsKey(variable.getKey())) {
                continue;
            }

            variables.put(variable.getKey(), variable.getValue());
        }

        for(Resources resources: settings.getResources()){
            resources.getTestCaseFile().getVariables(variables);
        }
    }

    public TestCase getTestCase(String name) {
        return testCaseTable.getTestCase(name);
    }

    @Nonnull
    public Iterator<UserKeyword> iterator() {
        return keywordTable.iterator();
    }

    public KeywordDefinition findUserKeyword(String name) {
        return getUserKeywords().findKeyword(name);
    }

    public Variable findVariable(String name) {
        return getVariables().get(name);
    }
}
