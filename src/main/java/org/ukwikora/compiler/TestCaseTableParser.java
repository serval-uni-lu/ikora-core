package org.ukwikora.compiler;

import org.ukwikora.model.StatementTable;
import org.ukwikora.model.TestCase;

import java.io.IOException;

class TestCaseTableParser {
    private TestCaseTableParser() {}

    static public StatementTable<TestCase> parse(LineReader reader) throws IOException {
        StatementTable<TestCase> testCaseTable = new StatementTable<>();

        reader.readLine();

        while(reader.getCurrent().isValid() && !LexerUtils.isBlock(reader.getCurrent().getText())){
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
