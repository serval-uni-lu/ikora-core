package org.ikora.builder;

import org.ikora.error.ErrorManager;
import org.ikora.model.Settings;
import org.ikora.model.UserKeyword;
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

    private Settings createSettings(String text, ErrorManager errors) throws IOException {
        Reader targetReader = new StringReader(text);
        LineReader reader = new LineReader(targetReader);
        reader.readLine();

        return SettingsTableParser.parse(reader, null, errors);
    }
}
