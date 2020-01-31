package org.ikora.builder;

import org.ikora.exception.InvalidArgumentException;
import org.ikora.exception.InvalidDependencyException;
import org.ikora.exception.MalformedVariableException;
import org.ikora.model.TimeOut;
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

    private static TimeOut parse(String string) throws IOException, InvalidArgumentException, MalformedVariableException, InvalidDependencyException {
        Line line = new Line(string, 0, false, false);
        Tokens tokens = LexerUtils.tokenize(line);

        return TimeoutParser.parse(tokens.withoutTag("\\[TimeOut\\]"));
    }
}
