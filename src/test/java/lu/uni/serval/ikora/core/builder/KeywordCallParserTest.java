package lu.uni.serval.ikora.core.builder;

import lu.uni.serval.ikora.core.Helpers;
import lu.uni.serval.ikora.core.error.ErrorManager;
import lu.uni.serval.ikora.core.model.Gherkin;
import lu.uni.serval.ikora.core.model.KeywordCall;
import lu.uni.serval.ikora.core.model.Token;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.*;

class KeywordCallParserTest {
    @Test
    void testSimpleCallNoArgs() throws IOException {
        final String text = "    Sleep";
        final ErrorManager errorManager = new ErrorManager();
        final KeywordCall call = createKeywordCall(text, errorManager, false);

        assertEquals("Sleep", call.getName());
        assertFalse(call.hasParameters());
    }

    @Test
    void testSimpleCallArgs() throws IOException {
        final String text = "    Log    Hello there";
        final ErrorManager errorManager = new ErrorManager();
        final KeywordCall call = createKeywordCall(text, errorManager, false);

        assertEquals("Log", call.getName());
        assertTrue(call.hasParameters());
    }

    @Test
    void testGherkinWithAccept() throws IOException {
        final String text = "    Given Log    Hello there";
        final ErrorManager errorManager = new ErrorManager();
        final KeywordCall call = createKeywordCall(text, errorManager, true);

        assertEquals("Log", call.getName());
        assertTrue(call.hasParameters());
        assertEquals(Gherkin.Type.GIVEN, call.getGherkin().getType());
    }

    @Test
    void testGherkinWithNoAccept() throws IOException {
        final String text = "    Given Log    Hello there";
        final ErrorManager errorManager = new ErrorManager();
        final KeywordCall call = createKeywordCall(text, errorManager, false);

        assertEquals("Given Log", call.getName());
        assertTrue(call.hasParameters());
        assertEquals(Gherkin.Type.NONE, call.getGherkin().getType());
    }

    void testContinuationToken() throws IOException {
        final String text = "    Given Log    \n...    Hello there";
        final ErrorManager errorManager = new ErrorManager();
        final KeywordCall call = createKeywordCall(text, errorManager, false);

        assertEquals("Given Log", call.getName());
        assertTrue(call.hasParameters());
        assertEquals("Hello there", call.getArgumentList().get(0).getName());
        assertEquals(Gherkin.Type.NONE, call.getGherkin().getType());
    }

    private KeywordCall createKeywordCall(String text, ErrorManager errors, boolean allowGherkin) throws IOException {
        final LineReader reader = Helpers.getLineReader(text);
        final Iterator<Token> tokenIterator = TokenScanner.from(LexerUtils.tokenize(reader))
                .skipTypes(Token.Type.CONTINUATION)
                .skipIndent(true)
                .iterator();

        return KeywordCallParser.parse(reader, tokenIterator.next(), tokenIterator, allowGherkin, errors);
    }
}