package lu.uni.serval.ikora.core.builder;

import lu.uni.serval.ikora.core.exception.InvalidArgumentException;
import lu.uni.serval.ikora.core.exception.MalformedVariableException;
import lu.uni.serval.ikora.core.model.TimeOut;
import lu.uni.serval.ikora.core.model.Tokens;
import lu.uni.serval.ikora.core.Helpers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class TimeoutParserTest {
    @ParameterizedTest
    @CsvSource({
            "[TimeOut]  1 minute 30 seconds,1 minute 30 seconds",
            "[TimeOut]  ${value},${value}",
            "[TimeOut]  None,None"
    })
    void testParseValidTimeoutWithRawValue(String source, String name) throws IOException, InvalidArgumentException, MalformedVariableException {
        TimeOut timeout = parse(source);
        assertTrue(timeout.isValid());
        assertEquals(name, timeout.getName());
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
