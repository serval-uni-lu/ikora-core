package org.ukwikora.builder;

import org.ukwikora.error.Error;
import org.ukwikora.model.UserKeyword;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserKeywordParserTest {
    @Test
    void checkSimple() throws IOException {
        String text = "Keyword with cool name\n" +
                "    Step one\tArg1\n" +
                "    Step two without args\n" +
                "    Step three \"Embedded arg\" \n";

        UserKeyword keyword = createKeyword(text);
        assertNotNull(keyword);

        assertEquals("Keyword with cool name", keyword.getName());
        assertEquals(3, keyword.getSteps().size());
        assertEquals("Step one", keyword.getStep(0).getName());
        assertEquals("Step two without args", keyword.getStep(1).getName());
        assertEquals("Step three \"Embedded arg\"", keyword.getStep(2).getName());
    }

    @Test
    void checkKeywordWithDocumentation() throws IOException {
        String text = "Keyword with cool name\n" +
                "    [Documentation]\tSimple documentation\n" +
                "    Step one\tArg1\n" +
                "    Step two without args\n" +
                "    Step three \"Embedded arg\" \n";

        UserKeyword keyword = createKeyword(text);
        assertNotNull(keyword);

        assertEquals("Keyword with cool name", keyword.getName());
        assertEquals(3, keyword.getSteps().size());
        assertEquals("Simple documentation", keyword.getDocumentation());
    }

    private UserKeyword createKeyword(String text) throws IOException {
        UserKeyword keyword = null;

        DynamicImports dynamicImports = new DynamicImports();
        Reader targetReader = new StringReader(text);
        LineReader reader = new LineReader(targetReader);
        reader.readLine();

        List<Error> errors = new ArrayList<>();
        keyword = UserKeywordParser.parse(reader, dynamicImports, errors);

        return keyword;
    }
}
