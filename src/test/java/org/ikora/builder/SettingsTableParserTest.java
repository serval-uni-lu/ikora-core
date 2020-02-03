package org.ikora.builder;

import org.ikora.error.ErrorManager;
import org.ikora.model.Settings;
import org.ikora.model.Tokens;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import static org.junit.jupiter.api.Assertions.*;

public class SettingsTableParserTest {
    @Test
    void testDocumentationParse() throws IOException {
        String settingText = "***Settings***\n" +
                            "Documentation    Example suite";

        ErrorManager errors = new ErrorManager();

        final Settings settings = createSettings(settingText, errors);

        assertEquals("Example suite", settings.getDocumentation());
    }

    @Test
    void testSuiteSetup() throws IOException {
        String settingText = "***Settings***\n" +
                "Suite Setup    Do Something    ${MESSAGE}";

        ErrorManager errors = new ErrorManager();

        final Settings settings = createSettings(settingText, errors);

        assertEquals("Do Something", settings.getSuiteSetup().getName().getText());
        assertEquals("${MESSAGE}", settings.getSuiteSetup().getArgumentList().get(0).getName().getText());
    }

    private Settings createSettings(String text, ErrorManager errors) throws IOException {
        Reader targetReader = new StringReader(text);
        LineReader reader = new LineReader(targetReader);
        Tokens tokens = LexerUtils.tokenize(reader.readLine());

        return SettingsTableParser.parse(reader, tokens,null, errors);
    }
}
