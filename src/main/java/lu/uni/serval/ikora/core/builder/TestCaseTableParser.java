package lu.uni.serval.ikora.core.builder;

import lu.uni.serval.ikora.core.error.ErrorManager;
import lu.uni.serval.ikora.core.model.SourceNodeTable;
import lu.uni.serval.ikora.core.model.TestCase;
import lu.uni.serval.ikora.core.model.Tokens;

import java.io.IOException;

class TestCaseTableParser {
    private TestCaseTableParser() {}

    public static SourceNodeTable<TestCase> parse(LineReader reader, Tokens blockTokens, DynamicImports dynamicImports, ErrorManager errors) throws IOException {
        SourceNodeTable<TestCase> testCaseTable = new SourceNodeTable<>();
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
