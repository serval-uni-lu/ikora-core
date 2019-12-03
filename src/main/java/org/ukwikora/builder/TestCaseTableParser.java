package org.ukwikora.builder;

import org.ukwikora.error.Error;
import org.ukwikora.model.NodeTable;
import org.ukwikora.model.TestCase;

import java.io.IOException;
import java.util.List;

class TestCaseTableParser {
    private TestCaseTableParser() {}

    public static NodeTable<TestCase> parse(LineReader reader, DynamicImports dynamicImports, List<Error> errors) throws IOException {
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
