package tech.ikora.builder;

import tech.ikora.error.ErrorManager;
import tech.ikora.model.NodeTable;
import tech.ikora.model.TestCase;
import tech.ikora.model.Tokens;

import java.io.IOException;

class TestCaseTableParser {
    private TestCaseTableParser() {}

    public static NodeTable<TestCase> parse(LineReader reader, Tokens blockTokens, DynamicImports dynamicImports, ErrorManager errors) throws IOException {
        NodeTable<TestCase> testCaseTable = new NodeTable<>();
        testCaseTable.setHeader(ParserUtils.parseHeaderName(reader, blockTokens, errors));

        while(reader.getCurrent().isValid() && !LexerUtils.isBlock(reader.getCurrent().getText())){
            if(reader.getCurrent().ignore()){
                reader.readLine();
                continue;
            }

            Tokens nameTokens = LexerUtils.tokenize(reader);

            TestCase testCase = TestCaseParser.parse(reader, nameTokens, dynamicImports, errors);
            testCaseTable.add(testCase);
        }

        return testCaseTable;
    }
}
