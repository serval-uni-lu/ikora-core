package org.ukwikora.compiler;

import org.junit.Test;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import static org.junit.Assert.*;

public class LexerUtilsTest {

    @Test
    public void checkRemoveIndent(){
        String[] twoIndents = {"","","Some text", "More text"};
        String[] oneIndent = {"","Some text", "More text"};
        String[] zeroIndents = {"Some text", "More text"};

        String[] twoIndentsClean = LexerUtils.removeIndent(twoIndents);
        String[] oneIndentClean = LexerUtils.removeIndent(oneIndent);
        String[] zeroIndentsClean = LexerUtils.removeIndent(zeroIndents);

        assertArrayEquals(zeroIndents, twoIndentsClean);
        assertArrayEquals(zeroIndents, oneIndentClean);
        assertArrayEquals(zeroIndents, zeroIndentsClean);
    }

    @Test
    public void checkTokenizerWith2spaceIndent(){
        String line = "  Some text";
        String[] tokens = LexerUtils.tokenize(line);

        assertEquals(tokens.length, 2);
        assertTrue(tokens[0].isEmpty());
        assertEquals(tokens[1], "Some text");
    }

    @Test
    public void checkTokenizeWith4spaceIndent(){
        String line = "    Some text";

        String[] tokens = LexerUtils.tokenize(line);

        assertEquals(2, tokens.length);
        assertTrue(tokens[0].isEmpty());
        assertEquals("Some text", tokens[1] );
    }

    @Test
    public void checkTokenizeWithTabIndent(){
        String line = "\tSome text";

        String[] tokens = LexerUtils.tokenize(line);

        assertEquals(2, tokens.length);
        assertTrue(tokens[0].isEmpty());
        assertEquals(tokens[1], "Some text");
    }

    @Test
    public void checkMultiLineDocumentation(){
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
    public void checkIsBlockWithoutBlockName(){
        assertTrue(LexerUtils.isBlock("***Block***"));
        assertTrue(LexerUtils.isBlock("***Block"));
        assertTrue(LexerUtils.isBlock("*** Block***"));
        assertTrue(LexerUtils.isBlock("***Block ***"));
        assertFalse(LexerUtils.isBlock("**Block***"));
        assertFalse(LexerUtils.isBlock("*Block***"));
        assertFalse(LexerUtils.isBlock("Block***"));
    }

    @Test
    public void checkIsBlockWithBlockName(){
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
