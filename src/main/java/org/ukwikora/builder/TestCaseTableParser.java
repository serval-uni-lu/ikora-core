package org.ukwikora.builder;

import org.ukwikora.model.NodeTable;
import org.ukwikora.model.TestCase;

class TestCaseTableParser {
    private TestCaseTableParser() {}

    public static NodeTable<TestCase> parse(LineReader reader, DynamicImports dynamicImports) throws Exception {
        NodeTable<TestCase> testCaseTable = new NodeTable<>();

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
