/*
 *
 *     Copyright © 2019 - 2022 University of Luxembourg
 *
 *     Licensed under the Apache License, Version 2.0 (the "License")
 *     you may not use this file except in compliance with the License.
 *     You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *     Unless required by applicable law or agreed to in writing, software
 *     distributed under the License is distributed on an "AS IS" BASIS,
 *     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *     See the License for the specific language governing permissions and
 *     limitations under the License.
 */
package lu.uni.serval.ikora.core.parser;

import lu.uni.serval.ikora.core.Helpers;
import lu.uni.serval.ikora.core.error.ErrorManager;
import lu.uni.serval.ikora.core.model.Argument;
import lu.uni.serval.ikora.core.model.NodeList;
import lu.uni.serval.ikora.core.model.Token;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.*;

class ArgumentsParserTest {
    @Test
    void testSingleArgument() throws IOException {
        final String text = "\t[arguments]\t${arg1}";
        final LineReader reader = Helpers.getLineReader(text);
        final Iterator<Token> tokenIterator = Helpers.getTokenIterator(reader);
        final ErrorManager errors = new ErrorManager();

        final NodeList<Argument> arguments = ArgumentsParser.parse(reader, tokenIterator.next(), tokenIterator, errors);

        assertTrue(errors.isEmpty());
        assertEquals(1, arguments.size());
        assertTrue(arguments.get(0).matches(Token.fromString("${arg1}")));
    }

    @Test
    void testMultipleArguments() throws IOException {
        final String text = "\t[arguments]\t${arg1}\t@{arg2}\t&{arg3}";
        final LineReader reader = Helpers.getLineReader(text);
        final Iterator<Token> tokenIterator = Helpers.getTokenIterator(reader);
        final ErrorManager errors = new ErrorManager();

        final NodeList<Argument> arguments = ArgumentsParser.parse(reader, tokenIterator.next(), tokenIterator, errors);

        assertTrue(errors.isEmpty());
        assertEquals(3, arguments.size());
        assertTrue(arguments.get(0).matches(Token.fromString("${arg1}")));
        assertTrue(arguments.get(1).matches(Token.fromString("@{arg2}")));
        assertTrue(arguments.get(2).matches(Token.fromString("&{arg3}")));
    }

    @Test
    void testNoArgument() throws IOException {
        final String text = "\t[arguments]";
        final LineReader reader = Helpers.getLineReader(text);
        final Iterator<Token> tokenIterator = Helpers.getTokenIterator(reader);
        final ErrorManager errors = new ErrorManager();

        final NodeList<Argument> arguments = ArgumentsParser.parse(reader, tokenIterator.next(), tokenIterator, errors);

        assertTrue(errors.isEmpty());
        assertEquals(0, arguments.size());
    }

    @Test
    void testInvalidArgument() throws IOException {
        final String text = "\t[arguments]\t{arg1}";
        final LineReader reader = Helpers.getLineReader(text);
        final Iterator<Token> tokenIterator = Helpers.getTokenIterator(reader);
        final ErrorManager errors = new ErrorManager();

        final NodeList<Argument> arguments = ArgumentsParser.parse(reader, tokenIterator.next(), tokenIterator, errors);

        assertFalse(errors.isEmpty());
        assertEquals(0, arguments.size());
    }
}
