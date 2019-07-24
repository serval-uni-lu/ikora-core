package org.ukwikora.builder;

import org.ukwikora.model.StatementTable;
import org.ukwikora.model.TestCase;

class TestCaseTableParser {
    private TestCaseTableParser() {}

    public static StatementTable<TestCase> parse(LineReader reader, DynamicImports dynamicImports) throws Exception {
        StatementTable<TestCase> testCaseTable = new StatementTable<>();

        reader.readLine();

        while(reader.getCurrent().isValid() && !LexerUtils.isBlock(reader.getCurrent().getText())){
            if(reader.getCurrent().ignore()){
                reader.readLine();
                continue;
            }

            TestCase testCase = TestCaseParser.parse(reader, dynamicImports);
            testCaseTable.add(testCase);
        }

        return testCaseTable;
    }
}
