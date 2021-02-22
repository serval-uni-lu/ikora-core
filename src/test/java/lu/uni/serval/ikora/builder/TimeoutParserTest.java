package lu.uni.serval.ikora.builder;

import lu.uni.serval.ikora.exception.InvalidArgumentException;
import lu.uni.serval.ikora.exception.MalformedVariableException;
import lu.uni.serval.ikora.model.TimeOut;
import lu.uni.serval.ikora.model.Tokens;
import lu.uni.serval.ikora.Helpers;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class TimeoutParserTest {
    @Test
    void testParseValidTimeoutWithRawValue() throws IOException, InvalidArgumentException, MalformedVariableException {
        TimeOut timeout = parse("[TimeOut]  1 minute 30 seconds");
        assertTrue(timeout.isValid());
        assertEquals("1 minute 30 seconds", timeout.getName());
    }

    @Test
    void testParseValidTimeoutWithVariable() throws IOException, InvalidArgumentException, MalformedVariableException {
        TimeOut timeout = parse("[TimeOut]  ${value}");
        assertTrue(timeout.isValid());
        assertEquals("${value}", timeout.getName());
    }

    @Test
    void testParseNoneTimeout() throws IOException, InvalidArgumentException, MalformedVariableException {
        TimeOut timeout = parse("[TimeOut]  None");
        assertTrue(timeout.isValid());
        assertEquals("None", timeout.getName());
    }

    @Test
    void testParseInvalidTimeout() {
        assertThrows(InvalidArgumentException.class, () -> parse("[TimeOut]  Invalid timeout"));
    }

    private static TimeOut parse(String text) throws IOException, InvalidArgumentException, MalformedVariableException {
        LineReader reader = Helpers.lineReader(text);
        Tokens tokens = LexerUtils.tokenize(reader);

        return TimeoutParser.parse(tokens.withoutTag("\\[TimeOut\\]"));
    }
}
