package org.ikora.builder;

import org.ikora.Helpers;
import org.ikora.error.ErrorManager;
import org.ikora.model.Settings;
import org.ikora.model.Tokens;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class SettingsTableParserTest {
    @Test
    void testDocumentationParse() throws IOException {
        String settingText = "***Settings***\n\n" +
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

    @Test
    void testSetup() throws IOException {
        String settingText = "***Settings***\n" +
                "Test Setup    Do Something    ${MESSAGE}";

        ErrorManager errors = new ErrorManager();

        final Settings settings = createSettings(settingText, errors);

        assertEquals("Do Something", settings.getTestSetup().getName().getText());
        assertEquals("${MESSAGE}", settings.getTestSetup().getArgumentList().get(0).getName().getText());
    }

    @Test
    void testSuiteTeardown() throws IOException {
        String settingText = "***Settings***\n" +
                "Suite Teardown    Do Something    ${MESSAGE}";

        ErrorManager errors = new ErrorManager();

        final Settings settings = createSettings(settingText, errors);

        assertEquals("Do Something", settings.getSuiteTeardown().getName().getText());
        assertEquals("${MESSAGE}", settings.getSuiteTeardown().getArgumentList().get(0).getName().getText());
    }

    @Test
    void tesTearDown() throws IOException {
        String settingText = "***Settings***\n" +
                "Test Teardown    Do Something    ${MESSAGE}";

        ErrorManager errors = new ErrorManager();

        final Settings settings = createSettings(settingText, errors);

        assertEquals("Do Something", settings.getTestTeardown().getName().getText());
        assertEquals("${MESSAGE}", settings.getTestTeardown().getArgumentList().get(0).getName().getText());
    }

    private Settings createSettings(String text, ErrorManager errors) throws IOException {
        LineReader reader = Helpers.lineReader(text);
        Tokens tokens = LexerUtils.tokenize(reader);

        return SettingsTableParser.parse(reader, tokens,null, errors);
    }
}
