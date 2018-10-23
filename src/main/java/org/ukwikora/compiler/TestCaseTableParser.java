package org.ukwikora.compiler;

import org.ukwikora.model.ElementTable;
import org.ukwikora.model.TestCase;

import java.io.IOException;

public class TestCaseTableParser {
    private TestCaseTableParser() {}

    static public ElementTable<TestCase> parse(LineReader reader) throws IOException {
        ElementTable<TestCase> testCaseTable = new ElementTable<>();

        reader.readLine();

        while(reader.getCurrent().isValid() && !Utils.isBlock(reader.getCurrent().getText())){
            if(reader.getCurrent().ignore()){
                reader.readLine();
                continue;
            }

            TestCase testCase = TestCaseParser.parse(reader);
            testCaseTable.add(testCase);
        }

        return testCaseTable;
    }
}
