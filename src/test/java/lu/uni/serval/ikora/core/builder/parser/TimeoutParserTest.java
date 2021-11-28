package lu.uni.serval.ikora.core.builder.parser;

/*-
 * #%L
 * Ikora Core
 * %%
 * Copyright (C) 2019 - 2021 University of Luxembourg
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License")
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import lu.uni.serval.ikora.core.exception.InvalidArgumentException;
import lu.uni.serval.ikora.core.exception.MalformedVariableException;
import lu.uni.serval.ikora.core.model.TimeOut;
import lu.uni.serval.ikora.core.model.Token;
import lu.uni.serval.ikora.core.Helpers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.io.IOException;
import java.util.Iterator;

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
    void testParseInvalidTimeout() throws IOException {
        assertFalse(parse("[TimeOut]  Invalid timeout").isValid());
    }

    private static TimeOut parse(String text) throws IOException {
        final LineReader reader = Helpers.getLineReader(text);
        final Iterator<Token> tokenIterator = TokenScanner.from(LexerUtils.tokenize(reader))
                .skipTypes(Token.Type.CONTINUATION)
                .iterator();

        return TimeoutParser.parse(tokenIterator.next(), tokenIterator);
    }
}
