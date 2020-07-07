package tech.ikora.builder;

import tech.ikora.error.ErrorManager;
import tech.ikora.model.TestCase;
import tech.ikora.model.Tokens;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import static org.junit.jupiter.api.Assertions.*;

public class TestCaseParserTest {
    @Test
    void testTestCaseParsingWithDocumentation() throws IOException {
        String text = "Test case with documentation\n" +
                "\t[Documentation]\tSome documentation\n" +
                "...\twith continuation\n\n" +
                "\tSome keyword";

        ErrorManager errors = new ErrorManager();
        TestCase testCase = createTestCase(text, errors);

        assertNotNull(testCase);
        assertEquals("Test case with documentation", testCase.getName());
        assertEquals(1, testCase.getSteps().size());
        assertEquals("Some documentationwith continuation", testCase.getDocumentation().clean().toString());
        assertEquals("Some keyword", testCase.getStep(0).getName());
    }

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
        assertEquals("Test case with control flow elements", testCase.getName());
        assertEquals(3, testCase.getSteps().size());
        assertEquals("First Keyword from project B", testCase.getStep(0).getName());
        assertEquals(":FOR", testCase.getStep(1).getName());
        assertEquals("Run Keyword If", testCase.getStep(2).getName());
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
