package tech.ikora.builder;

import tech.ikora.Helpers;
import tech.ikora.error.ErrorManager;
import tech.ikora.error.ErrorMessages;
import tech.ikora.model.Settings;
import tech.ikora.model.Tokens;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class SettingsTableParserTest {
    @Test
    void testDocumentationParse() throws IOException {
        String settingText = "***Settings***\n\n" +
                            "Documentation    Example suite";

        ErrorManager errors = new ErrorManager();

        final Settings settings = createSettings(settingText, errors);
        assertTrue(errors.in(null).isEmpty());

        assertEquals("Example suite", settings.getDocumentation());
    }

    @Test
    void testSuiteSetup() throws IOException {
        String settingText = "***Settings***\n" +
                "Suite Setup    Do Something    ${MESSAGE}";

        ErrorManager errors = new ErrorManager();
        assertTrue(errors.in(null).isEmpty());

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
        assertTrue(errors.in(null).isEmpty());

        assertEquals("Do Something", settings.getTestSetup().getName().getText());
        assertEquals("${MESSAGE}", settings.getTestSetup().getArgumentList().get(0).getName().getText());
    }

    @Test
    void testSuiteTeardown() throws IOException {
        String settingText = "***Settings***\n" +
                "Suite Teardown    Do Something    ${MESSAGE}";

        ErrorManager errors = new ErrorManager();

        final Settings settings = createSettings(settingText, errors);
        assertTrue(errors.in(null).isEmpty());

        assertEquals("Do Something", settings.getSuiteTeardown().getName().getText());
        assertEquals("${MESSAGE}", settings.getSuiteTeardown().getArgumentList().get(0).getName().getText());
    }

    @Test
    void tesTearDown() throws IOException {
        String settingText = "***Settings***\n" +
                "Test Teardown    Do Something    ${MESSAGE}";

        ErrorManager errors = new ErrorManager();

        final Settings settings = createSettings(settingText, errors);
        assertTrue(errors.in(null).isEmpty());

        assertEquals("Do Something", settings.getTestTeardown().getName().getText());
        assertEquals("${MESSAGE}", settings.getTestTeardown().getArgumentList().get(0).getName().getText());
    }

    @Test
    void tesTemplate() throws IOException {
        String settingText = "***Settings***\n" +
                "Test Template    Do Something    ${MESSAGE}";

        ErrorManager errors = new ErrorManager();

        final Settings settings = createSettings(settingText, errors);
        assertTrue(errors.in(null).isEmpty());

        assertEquals("Do Something", settings.getTemplate().getName().getText());
        assertEquals("${MESSAGE}", settings.getTemplate().getArgumentList().get(0).getName().getText());
    }

    @Test
    void tesTemplateWithInvalidType() throws IOException {
        String settingText = "***Settings***\n" +
                "Test Template    ${RESULT}=  Do Something    ${MESSAGE}";

        ErrorManager errors = new ErrorManager();
        createSettings(settingText, errors);

        assertEquals(1, errors.in(null).getSize());

        String errorMessage = errors.in(null).getSyntaxErrors().iterator().next().getMessage();
        assertEquals(ErrorMessages.FAILED_TO_PARSE_TEMPLATE, errorMessage);
    }

    private Settings createSettings(String text, ErrorManager errors) throws IOException {
        LineReader reader = Helpers.lineReader(text);
        Tokens tokens = LexerUtils.tokenize(reader);

        return SettingsTableParser.parse(reader, tokens,null, errors);
    }
}
