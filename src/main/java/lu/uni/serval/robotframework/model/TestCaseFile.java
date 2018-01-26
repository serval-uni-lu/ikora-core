package lu.uni.serval.robotframework.model;

import java.util.Iterator;

public class TestCaseFile implements Iterable<TestCase> {
    private String directory;
    private String name;
    private Settings settings;
    private TestCaseTable testCaseTable;
    private KeywordTable keywordTable;
    private VariableTable variableTable;

    public TestCaseFile(String directory, String name, Settings settings, TestCaseTable testCaseTable,
                        KeywordTable keywordTable, VariableTable variableTable) {
        this.directory = directory;
        this.name = name;
        this.settings = settings;
        this.testCaseTable = testCaseTable;
        this.keywordTable = keywordTable;
        this.variableTable = variableTable;
    }

    public String getDirectory(){
        return this.directory;
    }

    public String getName() {
        return name;
    }

    public Settings getSettings() {
        return this.settings;
    }

    public Iterator<TestCase> iterator() {
        return testCaseTable.iterator();
    }

    public UserKeyword findUserKeyword(Step step) {
        for(UserKeyword userKeyword : keywordTable) {
            if (userKeyword.isEqual(step)) {
                return userKeyword;
            }
        }

        for (Resources resources : settings.getResources()) {
            if(resources.getType() != Resources.Type.Resource) {
                continue;
            }

            UserKeyword userKeyword = resources.getFile().findUserKeyword(step);

            if(userKeyword != null) {
                return userKeyword;
            }
        }

        return new UserKeyword(step);
    }
}
