package tech.ikora.builder;

import tech.ikora.Helpers;
import tech.ikora.error.ErrorManager;
import tech.ikora.error.ErrorMessages;
import tech.ikora.model.Settings;
import tech.ikora.model.Token;
import tech.ikora.model.Tokens;
import org.junit.jupiter.api.Test;

import java.io.File;
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

    @Test
    void testMetadata() throws IOException {
        String settingText = "***Settings***\n" +
                "Metadata    Version    2.0";

        ErrorManager errors = new ErrorManager();

        final Settings settings = createSettings(settingText, errors);
        assertTrue(errors.in(null).isEmpty());

        assertEquals(1, settings.getMetadata().size());
        assertEquals("2.0", settings.getMetadata("Version").getText());
    }

    @Test
    void testMetadataWithNoValue() throws IOException {
        String settingText = "***Settings***\n" +
                "Metadata    Version";

        ErrorManager errors = new ErrorManager();

        final Settings settings = createSettings(settingText, errors);
        assertEquals(1, errors.in(new File("<null>")).getSize());

        assertEquals(0, settings.getMetadata().size());
    }

    @Test
    void testMetadataWithTooManyValue() throws IOException {
        final String settingText = "***Settings***\n" +
                "Metadata    Version    2.0    Release";

        final ErrorManager errors = new ErrorManager();
        final Settings settings = createSettings(settingText, errors);
        assertEquals(1, errors.in(new File("<null>")).getSize());

        assertEquals(1, settings.getMetadata().size());
        assertEquals("2.0", settings.getMetadata("Version").getText());
    }

    @Test
    void testAddDefaultTags() throws IOException {
        final String settingText = "***Settings***\n" +
                "Default Tags    default tag 1    default tag 2    default tag 3    default tag 4    default tag 5";

        final ErrorManager errors = new ErrorManager();
        final Settings settings = createSettings(settingText, errors);
        assertTrue(errors.in(new File("\\")).isEmpty());

        assertTrue(settings.hasDefaultTag("default tag 1"));
        assertTrue(settings.hasDefaultTag("default tag 2"));
        assertTrue(settings.hasDefaultTag("default tag 3"));
        assertTrue(settings.hasDefaultTag("default tag 4"));
        assertTrue(settings.hasDefaultTag("default tag 5"));

        assertFalse(settings.hasDefaultTag("fake tag"));
    }

    @Test
    void testAddForceTags() throws IOException {
        final String settingText = "***Settings***\n" +
                "Force Tags    tag 1    tag 2    tag 3    tag 4    tag 5";

        final ErrorManager errors = new ErrorManager();
        final Settings settings = createSettings(settingText, errors);
        assertTrue(errors.in(new File("\\")).isEmpty());

        assertTrue(settings.hasForceTag("tag 1"));
        assertTrue(settings.hasForceTag("tag 2"));
        assertTrue(settings.hasForceTag("tag 3"));
        assertTrue(settings.hasForceTag("tag 4"));
        assertTrue(settings.hasForceTag("tag 5"));

        assertFalse(settings.hasForceTag("fake tag"));
    }

    @Test
    void testTestTimeout() throws IOException {
        final String settingText = "***Settings***\n" +
                "Test Timeout    2 minutes";

        final ErrorManager errors = new ErrorManager();
        final Settings settings = createSettings(settingText, errors);
        assertTrue(errors.in(new File("<null>")).isEmpty());

        assertEquals("2 minutes", settings.getTimeOut().getTokens().toString());
    }

    @Test
    void testVariableFile() throws IOException {
        final String settingText = "***Settings***\n" +
                "Variables    myvariables.py";

        final ErrorManager errors = new ErrorManager();
        final Settings settings = createSettings(settingText, errors);
        assertTrue(errors.in(new File("<null>")).isEmpty());

        assertEquals(1, settings.getVariableFiles().size());
    }

    @Test
    void testVariableFileWithParameters() throws IOException {
        final String settingText = "***Settings***\n" +
                "Variables    myvariables.py    arg1    ${ARG2}";

        final ErrorManager errors = new ErrorManager();
        final Settings settings = createSettings(settingText, errors);
        assertTrue(errors.in(new File("<null>")).isEmpty());

        assertEquals(1, settings.getVariableFiles().size());
    }

    private Settings createSettings(String text, ErrorManager errors) throws IOException {
        LineReader reader = Helpers.lineReader(text);
        Tokens tokens = LexerUtils.tokenize(reader);

        return SettingsTableParser.parse(reader, tokens,null, errors);
    }
}
