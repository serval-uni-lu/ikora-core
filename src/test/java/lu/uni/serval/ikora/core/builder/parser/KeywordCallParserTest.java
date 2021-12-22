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

    @Test
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
