package lu.uni.serval.robotframework.compiler;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class LineTest {

    @Test
    public void checkTokenizerWith2spaceIndent(){
        Line line = new Line("  [Documentation]", 0);

        String[] tokens = line.tokenize();

        assertEquals(tokens.length, 2);
        assertTrue(tokens[0].isEmpty());
        assertEquals(tokens[1], "[Documentation]");
    }

    @Test
    public void checkTokenizeWith4spaceIndent(){
        Line line = new Line("    [Documentation]", 0);

        String[] tokens = line.tokenize();

        assertEquals(2, tokens.length);
        assertTrue(tokens[0].isEmpty());
        assertEquals("[Documentation]", tokens[1] );
    }

    @Test
    public void checkTokenizeWithTabIndent(){
        Line line = new Line("\t[Documentation]", 0);

        String[] tokens = line.tokenize();

        assertEquals(2, tokens.length);
        assertTrue(tokens[0].isEmpty());
        assertEquals(tokens[1], "[Documentation]");
    }
}
