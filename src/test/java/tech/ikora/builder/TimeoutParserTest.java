package tech.ikora.builder;

import tech.ikora.Helpers;
import tech.ikora.exception.InvalidArgumentException;
import tech.ikora.exception.InvalidDependencyException;
import tech.ikora.exception.MalformedVariableException;
import tech.ikora.model.TimeOut;
import tech.ikora.model.Tokens;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class TimeoutParserTest {
    @Test
    void testParseValidTimeoutWithRawValue() throws IOException, InvalidArgumentException, MalformedVariableException, InvalidDependencyException {
        TimeOut timeout = parse("[TimeOut]  1 minute 30 seconds");
        assertTrue(timeout.isValid());
        assertEquals("1 minute 30 seconds", timeout.getName().getText());
    }

    @Test
    void testParseValidTimeoutWithVariable() throws IOException, InvalidArgumentException, MalformedVariableException, InvalidDependencyException {
        TimeOut timeout = parse("[TimeOut]  ${value}");
        assertTrue(timeout.isValid());
        assertEquals("${value}", timeout.getName().getText());
    }

    @Test
    void testParseNoneTimeout() throws IOException, InvalidArgumentException, MalformedVariableException, InvalidDependencyException {
        TimeOut timeout = parse("[TimeOut]  None");
        assertTrue(timeout.isValid());
        assertEquals("None", timeout.getName().getText());
    }

    @Test
    void testParseInvalidTimeout() {
        assertThrows(InvalidArgumentException.class, () -> parse("[TimeOut]  Invalid timeout"));
    }

    private static TimeOut parse(String text) throws IOException, InvalidArgumentException, MalformedVariableException, InvalidDependencyException {
        LineReader reader = Helpers.lineReader(text);
        Tokens tokens = LexerUtils.tokenize(reader);

        return TimeoutParser.parse(tokens.withoutTag("\\[TimeOut\\]"));
    }
}
