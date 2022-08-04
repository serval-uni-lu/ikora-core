package lu.uni.serval.ikora.core.parser;

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

import lu.uni.serval.ikora.core.model.Token;
import lu.uni.serval.ikora.core.model.Tokens;
import lu.uni.serval.ikora.core.Helpers;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.*;

class LexerUtilsTest {

    @Test
    void testTokenizerWith2spaceIndent() throws IOException {
        String line = "  Some text";
        Tokens tokens = LexerUtils.tokenize(Helpers.getLineReader(line));

        assertEquals(2, tokens.size());

        Iterator<Token> iterator =  tokens.iterator();

        Token token1 = iterator.next();
        assertTrue(token1.isDelimiter());

        Token token2 = iterator.next();
        assertTrue(token2.isText());
    }

    @Test
    void testTokenizerWith1spaceIndent() throws IOException {
        String line = " Some text";
        Tokens tokens = LexerUtils.tokenize(Helpers.getLineReader(line));

        assertEquals( 1, tokens.size());

        Token token = tokens.iterator().next();
        assertTrue(token.isText());
        assertEquals("Some text", token.getText());
    }

    @Test
    void testTokenizeWith4spaceIndent() throws IOException {
        String line = "    Some text";

        Tokens tokens = LexerUtils.tokenize(Helpers.getLineReader(line));

        assertEquals(2, tokens.size());

        Iterator<Token> iterator =  tokens.iterator();

        Token token1 = iterator.next();
        assertTrue(token1.isDelimiter());

        Token token2 = iterator.next();
        assertTrue(token2.isText());
    }

    @Test
    void testTokenizeWithTabIndent() throws IOException {
        String line = "\tSome text";

        Tokens tokens = LexerUtils.tokenize(Helpers.getLineReader(line));

        assertEquals(2, tokens.size());

        assertTrue(tokens.get(0).isDelimiter());
        assertTrue(tokens.get(1).isText());
    }

    @Test
    void testTokenizeWithComments() throws IOException {
        String line = "\tSome text #Comments in line";

        Tokens tokens = LexerUtils.tokenize(Helpers.getLineReader(line));

        assertEquals(3, tokens.size());

        Token comment = tokens.get(2);
        assertTrue(comment.isComment());
    }

    @Test
    void testTokenizeCommentLine() throws IOException {
        String line = "#Comments line with\tsome\tdelimiters";

        Tokens tokens = LexerUtils.tokenize(Helpers.getLineReader(line));

        assertEquals(1, tokens.size());
        assertTrue(tokens.get(0).isComment());
    }

    @Test
    void testTokenizeMultipleIndents() throws IOException {
        String line = "  \\  With 2 indents";

        Tokens tokens = LexerUtils.tokenize(Helpers.getLineReader(line));

        assertEquals(3, tokens.size());
        assertTrue(tokens.get(0).isDelimiter());
        assertTrue(tokens.get(1).isDelimiter());
        assertTrue(tokens.get(2).isText());
    }

    @Test
    void testEscapeComment() throws IOException {
        String line = "Text with escaped \\# comment";

        Tokens tokens = LexerUtils.tokenize(Helpers.getLineReader(line));

        assertEquals(1, tokens.size());
        assertTrue(tokens.get(0).isText());
    }

    @Test
    void testMultiLineDocumentation(){
        String documentation = "\t[Documentation]\tFirst line\n" +
                "\t...\tSecond line\n" +
                "\t...\tThird line\n" +
                "\tNot documentation";

        Tokens tokens = null;

        try {
            LineReader reader = createReader(documentation);
            tokens = LexerUtils.tokenize(reader);
        } catch (IOException e) {
            fail("Exception raised: " + e.getMessage());
        }

        String[] lines = tokens.toString().split("\n");

        assertEquals(3, lines.length);
        assertEquals("[Documentation] First line", lines[0].trim());
        assertEquals("Second line", lines[1].trim());
        assertEquals("Third line", lines[2].trim());
    }

    @Test
    void testMultilineWithIndent() throws IOException {
        final String text = "    Log\n" +
                "    ...  Test Status: ${TEST STATUS}";

        final Tokens tokens = LexerUtils.tokenize(Helpers.getLineReader(text));
        assertEquals(4, tokens.size());
        assertEquals("...", tokens.get(2).getText());
    }

    @Test
    void testIsBlockWithoutBlockName(){
        assertTrue(LexerUtils.isBlock("***Block***"));
        assertTrue(LexerUtils.isBlock("***Block"));
        assertTrue(LexerUtils.isBlock("*** Block***"));
        assertTrue(LexerUtils.isBlock("***Block ***"));
        assertFalse(LexerUtils.isBlock("**Block***"));
        assertFalse(LexerUtils.isBlock("*Block***"));
        assertFalse(LexerUtils.isBlock("Block***"));
    }

    @Test
    void testIsBlockWithBlockName(){
        assertTrue(LexerUtils.isBlock("***Block***", "Block"));
        assertTrue(LexerUtils.isBlock("***BLOCK***", "Block"));
        assertTrue(LexerUtils.isBlock("*** BLOCK***", "Block"));
    }

    private LineReader createReader(String text){
        LineReader reader = null;

        try {
            reader = new LineReader(text);
            reader.readLine();
        } catch (IOException e) {
            fail("Error while creating LineReader: " + e.getMessage());
        }

        return reader;
    }
}
