package org.ukwikora.compiler;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class LexerUtilsTest {

    @Test
    public void checkTokenizerWith2spaceIndent(){
        String line = "  [Documentation]";
        String[] tokens = LexerUtils.tokenize(line);

        assertEquals(tokens.length, 2);
        assertTrue(tokens[0].isEmpty());
        assertEquals(tokens[1], "[Documentation]");
    }

    @Test
    public void checkTokenizeWith4spaceIndent(){
        String line = "    [Documentation]";
        String[] tokens = LexerUtils.tokenize(line);

        assertEquals(2, tokens.length);
        assertTrue(tokens[0].isEmpty());
        assertEquals("[Documentation]", tokens[1] );
    }

    @Test
    public void checkTokenizeWithTabIndent(){
        String line = "\t[Documentation]";
        String[] tokens = LexerUtils.tokenize(line);

        assertEquals(2, tokens.length);
        assertTrue(tokens[0].isEmpty());
        assertEquals(tokens[1], "[Documentation]");
    }
}
