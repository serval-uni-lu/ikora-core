package org.ikora.builder;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.*;

class LexerUtilsTest {

    @Test
    void checkTokenizerWith2spaceIndent(){
        String line = "  Some text";
        Tokens tokens = LexerUtils.tokenize(new Line(line, 0, false, false));

        assertEquals(2, tokens.size());

        Iterator<Token> iterator =  tokens.iterator();

        Token token1 = iterator.next();
        assertTrue(token1.isDelimiter());

        Token token2 = iterator.next();
        assertTrue(token2.isText());
    }

    @Test
    void checkTokenizerWith1spaceIndent(){
        String line = " Some text";
        Tokens tokens = LexerUtils.tokenize(new Line(line, 0, false, false));

        assertEquals( 1, tokens.size());

        Token token = tokens.iterator().next();
        assertTrue(token.isText());
        assertEquals("Some text", token.getValue());
    }

    @Test
    void checkTokenizeWith4spaceIndent(){
        String line = "    Some text";

        Tokens tokens = LexerUtils.tokenize(new Line(line, 0, false, false));

        assertEquals(2, tokens.size());

        Iterator<Token> iterator =  tokens.iterator();

        Token token1 = iterator.next();
        assertTrue(token1.isDelimiter());

        Token token2 = iterator.next();
        assertTrue(token2.isText());
    }

    @Test
    void checkTokenizeWithTabIndent(){
        String line = "\tSome text";

        Tokens tokens = LexerUtils.tokenize(new Line(line, 0, false, false));

        assertEquals(2, tokens.size());

        Iterator<Token> iterator =  tokens.iterator();

        Token token1 = iterator.next();
        assertTrue(token1.isDelimiter());

        Token token2 = iterator.next();
        assertTrue(token2.isText());
    }

    @Test
    void checkMultiLineDocumentation(){
        String documentation = "\t[Documentation]\tFirst line\n" +
                "\t...\tSecond line\n" +
                "\t...\tThird line\n" +
                "\tNot documentation";

        StringBuilder builder = new StringBuilder();

        try {
            LineReader reader = createReader(documentation);
            LexerUtils.parseDocumentation(reader, builder);
        } catch (IOException e) {
            fail("Exception raised: " + e.getMessage());
        }

        String[] lines = builder.toString().split("\n");

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
            Reader targetReader = new StringReader(text);
            reader = new LineReader(targetReader);
            reader.readLine();
        } catch (IOException e) {
            fail("Error while creating LineReader: " + e.getMessage());
        }

        return reader;
    }
}
