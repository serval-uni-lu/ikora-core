package lu.uni.serval.robotframework.compiler;

import lu.uni.serval.robotframework.model.KeywordTable;
import lu.uni.serval.robotframework.model.TestCase;

import java.io.IOException;

public class TestCaseTableParser {
    private TestCaseTableParser() {}

    static public KeywordTable<TestCase> parse(LineReader reader) throws IOException {
        KeywordTable<TestCase> testCaseTable = new KeywordTable<>();

        reader.readLine();

        while(reader.getCurrent().isValid() && !Utils.isBlock(reader.getCurrent().getText())){
            if(Utils.ignore(reader.getCurrent())){
                reader.readLine();
                continue;
            }

            TestCase testCase = TestCaseParser.parse(reader);
            testCaseTable.add(testCase);
        }

        return testCaseTable;
    }
}
