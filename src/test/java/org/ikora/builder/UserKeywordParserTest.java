package org.ikora.builder;

import org.ikora.error.ErrorManager;
import org.ikora.model.UserKeyword;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import static org.junit.jupiter.api.Assertions.*;

class UserKeywordParserTest {
    @Test
    void testParseSimpleKeyword() throws IOException {
        String text = "Keyword with cool name\n" +
                "    Step one\tArg1\n" +
                "    Step two without args\n" +
                "    Step three \"Embedded arg\" \n";

        ErrorManager errors = new ErrorManager();
        UserKeyword keyword = createKeyword(text, errors);
        assertNotNull(keyword);
        assertTrue(errors.isEmpty());

        assertEquals("Keyword with cool name", keyword.getName().getText());
        assertEquals(3, keyword.getSteps().size());
        assertEquals("Step one", keyword.getStep(0).getName().getText());
        assertEquals("Step two without args", keyword.getStep(1).getName().getText());
        assertEquals("Step three \"Embedded arg\"", keyword.getStep(2).getName().getText());
    }

    @Test
    void testParseDocumentation() throws IOException {
        String text = "Keyword with documentation\n" +
                "    [Documentation]\tSimple documentation\n" +
                "    Step one\tArg1\n" +
                "    Step two without args\n" +
                "    Step three \"Embedded arg\" \n";

        ErrorManager errors = new ErrorManager();
        UserKeyword keyword = createKeyword(text, errors);
        assertNotNull(keyword);
        assertTrue(errors.isEmpty());

        assertEquals("Keyword with documentation", keyword.getName().getText());
        assertEquals(3, keyword.getSteps().size());
        assertEquals("Simple documentation", keyword.getDocumentation());
    }

    @Test
    void testParseTags() throws IOException {
        String text = "Keyword with tags\n" +
                "   [Tags]\ttag1\ttag2 with space\ttag3\n" +
                "   Step1\n";

        ErrorManager errors = new ErrorManager();
        UserKeyword keyword = createKeyword(text, errors);
        assertNotNull(keyword);
        assertTrue(errors.isEmpty());

        assertEquals("Keyword with tags", keyword.getName().getText());
        assertEquals(3, keyword.getTags().size());
        assertTrue(keyword.getTags().contains("tag1"));
        assertTrue(keyword.getTags().contains("tag2 with space"));
        assertTrue(keyword.getTags().contains("tag3"));
    }

    private UserKeyword createKeyword(String text, ErrorManager errors) throws IOException {
        DynamicImports dynamicImports = new DynamicImports();
        Reader targetReader = new StringReader(text);
        LineReader reader = new LineReader(targetReader);
        reader.readLine();

        return UserKeywordParser.parse(reader, dynamicImports, errors);
    }
}
