package lu.uni.serval.robotframework.compiler;

import lu.uni.serval.robotframework.model.TestCase;
import lu.uni.serval.robotframework.model.TestCaseTable;

import java.io.IOException;

public class TestCaseTableParser {
    private TestCaseTableParser() {}

    static public TestCaseTable parse(LineReader reader) throws IOException {
        TestCaseTable testCaseTable = new TestCaseTable();

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
