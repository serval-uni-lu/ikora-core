package org.ukwikora.builder;

import org.ukwikora.error.ErrorManager;
import org.ukwikora.model.NodeTable;
import org.ukwikora.model.TestCase;

import java.io.IOException;

class TestCaseTableParser {
    private TestCaseTableParser() {}

    public static NodeTable<TestCase> parse(LineReader reader, DynamicImports dynamicImports, ErrorManager errors) throws IOException {
        NodeTable<TestCase> testCaseTable = new NodeTable<>();

        reader.readLine();

        while(reader.getCurrent().isValid() && !LexerUtils.isBlock(reader.getCurrent().getText())){
            if(reader.getCurrent().ignore()){
                reader.readLine();
                continue;
            }

            TestCase testCase = TestCaseParser.parse(reader, dynamicImports, errors);
            testCaseTable.add(testCase);
        }

        return testCaseTable;
    }
}
