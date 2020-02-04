package org.ikora.builder;

import org.ikora.error.ErrorManager;
import org.ikora.model.TestCase;
import org.ikora.model.Tokens;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import static org.junit.jupiter.api.Assertions.*;

public class TestCaseParserTest {
    @Test
    void testTestCaseParsingContainingForLoop() throws IOException {
        String text = "Test case with control flow elements\n" +
                    "    First Keyword from project B\n" +
                    "    :FOR    ${INDEX}    IN RANGE    1    3\n" +
                    "    \\    Log    ${INDEX}\n" +
                    "    Run Keyword If  ${True}  Log  ${conditional}\n";

        ErrorManager errors = new ErrorManager();
        TestCase testCase = createTestCase(text, errors);

        assertNotNull(testCase);
        assertEquals("Test case with control flow elements", testCase.getName().getText());
        assertEquals(3, testCase.getSteps().size());
        assertEquals("First Keyword from project B", testCase.getStep(0).getName().getText());
        assertEquals(":FOR", testCase.getStep(1).getName().getText());
        assertEquals("Run Keyword If", testCase.getStep(2).getName().getText());
    }

    private TestCase createTestCase(String text, ErrorManager errors) throws IOException {
        DynamicImports dynamicImports = new DynamicImports();
        Reader targetReader = new StringReader(text);
        LineReader reader = new LineReader(targetReader);
        reader.readLine();

        Tokens tokens = LexerUtils.tokenize(reader);

        return TestCaseParser.parse(reader, tokens, dynamicImports, errors);
    }
}
