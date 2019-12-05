package org.ikora.builder;

import org.ikora.error.ErrorManager;
import org.ikora.model.NodeTable;
import org.ikora.model.TestCase;

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
