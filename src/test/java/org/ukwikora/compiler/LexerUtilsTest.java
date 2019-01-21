package org.ukwikora.compiler;

import org.junit.Test;

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
}
