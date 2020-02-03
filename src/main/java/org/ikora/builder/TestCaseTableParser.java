package org.ikora.builder;

import org.ikora.error.ErrorManager;
import org.ikora.model.NodeTable;
import org.ikora.model.Position;
import org.ikora.model.TestCase;
import org.ikora.model.Tokens;

import java.io.IOException;

class TestCaseTableParser {
    private TestCaseTableParser() {}

    public static NodeTable<TestCase> parse(LineReader reader, Tokens blockTokens, DynamicImports dynamicImports, ErrorManager errors) throws IOException {
        NodeTable<TestCase> testCaseTable = new NodeTable<>();
        testCaseTable.setHeader(ParserUtils.parseHeaderName(reader, blockTokens, errors));

        if(blockTokens.size() == 1){
            testCaseTable.setHeader(blockTokens.first());
        }
        else{
            errors.registerSyntaxError(
                    reader.getFile(),
                    "Block name for test case not valid",
                    Position.fromTokens(blockTokens, reader.getCurrent())
            );
        }

        reader.readLine();

        while(reader.getCurrent().isValid() && !LexerUtils.isBlock(reader.getCurrent().getText())){
            if(reader.getCurrent().ignore()){
                reader.readLine();
                continue;
            }

            Tokens nameTokens = LexerUtils.tokenize(reader.getCurrent());

            TestCase testCase = TestCaseParser.parse(reader, nameTokens, dynamicImports, errors);
            testCaseTable.add(testCase);
        }

        return testCaseTable;
    }
}
