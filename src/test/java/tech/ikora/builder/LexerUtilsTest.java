package tech.ikora.builder;

import tech.ikora.Helpers;
import tech.ikora.model.Token;
import tech.ikora.model.Tokens;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.*;

class LexerUtilsTest {

    @Test
    void checkTokenizerWith2spaceIndent() throws IOException {
        String line = "  Some text";
        Tokens tokens = LexerUtils.tokenize(Helpers.lineReader(line));

        assertEquals(2, tokens.size());

        Iterator<Token> iterator =  tokens.iterator();

        Token token1 = iterator.next();
        assertTrue(token1.isDelimiter());

        Token token2 = iterator.next();
        assertTrue(token2.isText());
    }

    @Test
    void checkTokenizerWith1spaceIndent() throws IOException {
        String line = " Some text";
        Tokens tokens = LexerUtils.tokenize(Helpers.lineReader(line));

        assertEquals( 1, tokens.size());

        Token token = tokens.iterator().next();
        assertTrue(token.isText());
        assertEquals("Some text", token.getText());
    }

    @Test
    void checkTokenizeWith4spaceIndent() throws IOException {
        String line = "    Some text";

        Tokens tokens = LexerUtils.tokenize(Helpers.lineReader(line));

        assertEquals(2, tokens.size());

        Iterator<Token> iterator =  tokens.iterator();

        Token token1 = iterator.next();
        assertTrue(token1.isDelimiter());

        Token token2 = iterator.next();
        assertTrue(token2.isText());
    }

    @Test
    void checkTokenizeWithTabIndent() throws IOException {
        String line = "\tSome text";

        Tokens tokens = LexerUtils.tokenize(Helpers.lineReader(line));

        assertEquals(2, tokens.size());

        assertTrue(tokens.get(0).isDelimiter());
        assertTrue(tokens.get(1).isText());
    }

    @Test
    void checkTokenizeWithComments() throws IOException {
        String line = "\tSome text #Comments in line";

        Tokens tokens = LexerUtils.tokenize(Helpers.lineReader(line));

        assertEquals(3, tokens.size());

        Token comment = tokens.get(2);
        assertTrue(comment.isComment());
    }

    @Test
    void testTokenizeCommentLine() throws IOException {
        String line = "#Comments line with\tsome\tdelimiters";

        Tokens tokens = LexerUtils.tokenize(Helpers.lineReader(line));

        assertEquals(1, tokens.size());
        assertTrue(tokens.get(0).isComment());
    }

    @Test
    void testTokenizeMultipleIndents() throws IOException {
        String line = "  \\  With 2 indents";

        Tokens tokens = LexerUtils.tokenize(Helpers.lineReader(line));

        assertEquals(3, tokens.size());
        assertTrue(tokens.get(0).isDelimiter());
        assertTrue(tokens.get(1).isDelimiter());
        assertTrue(tokens.get(2).isText());
    }

    @Test
    void testEscapeComment() throws IOException {
        String line = "Text with escaped \\# comment";

        Tokens tokens = LexerUtils.tokenize(Helpers.lineReader(line));

        assertEquals(1, tokens.size());
        assertTrue(tokens.get(0).isText());
    }

    @Test
    void checkMultiLineDocumentation(){
        String documentation = "\t[Documentation]\tFirst line\n" +
                "\t...\tSecond line\n" +
                "\t...\tThird line\n" +
                "\tNot documentation";

        Tokens tokens = null;

        try {
            LineReader reader = createReader(documentation);
            tokens = LexerUtils.tokenize(reader).withoutIndent().withoutTag("\\[documentation\\]");
        } catch (IOException e) {
            fail("Exception raised: " + e.getMessage());
        }

        String[] lines = tokens.toString().split("\n");

        assertEquals(3, lines.length);
        assertEquals("First line", lines[0]);
        assertEquals("Second line", lines[1]);
        assertEquals("Third line", lines[2]);
    }

    @Test
    void checkIsBlockWithoutBlockName(){
        assertTrue(LexerUtils.isBlock("***Block***"));
        assertTrue(LexerUtils.isBlock("***Block"));
        assertTrue(LexerUtils.isBlock("*** Block***"));
        assertTrue(LexerUtils.isBlock("***Block ***"));
        assertFalse(LexerUtils.isBlock("**Block***"));
        assertFalse(LexerUtils.isBlock("*Block***"));
        assertFalse(LexerUtils.isBlock("Block***"));
    }

    @Test
    void checkIsBlockWithBlockName(){
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
