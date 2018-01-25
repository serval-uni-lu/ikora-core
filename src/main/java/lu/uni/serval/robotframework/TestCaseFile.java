package lu.uni.serval.robotframework;

public class TestCaseFile {
    private String directory;
    private String name;
    private Settings settings;
    private TestCaseTable testCaseTable;
    private KeywordTable keywordTable;
    private VariableTable variableTable;

    public TestCaseFile(String directory, String name, Settings settings, TestCaseTable testCaseTable,
                        KeywordTable keywordTable, VariableTable variableTable) {
        this.name = name;
        this.settings = settings;
        this.testCaseTable = testCaseTable;
        this.keywordTable = keywordTable;
        this.variableTable = variableTable;
    }
}
