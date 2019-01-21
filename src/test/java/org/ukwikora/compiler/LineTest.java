package org.ukwikora.compiler;

import org.junit.Test;

import static org.junit.Assert.*;

public class LineTest {
    @Test
    public void checkSimpleLine(){
        Line simple = new Line("simple line", 4, false, false);

        assertTrue(simple.isValid());
        assertEquals("simple line", simple.getText());
        assertEquals(4, simple.getNumber());
        assertFalse(simple.isComment());
        assertFalse(simple.isEmpty());
        assertFalse(simple.ignore());
    }

    @Test
    public void checkInvalidLine(){
        Line invalid = new Line(null, 3, false, true);

        assertFalse(invalid.isValid());
        assertNull(invalid.getText());
        assertEquals(3, invalid.getNumber());
        assertFalse(invalid.isComment());
        assertTrue(invalid.isEmpty());
        assertTrue(invalid.ignore());
    }

    @Test
    public void checkEmptyLine(){
        Line invalid = new Line("", 12, false, true);

        assertTrue(invalid.isValid());
        assertEquals("", invalid.getText());
        assertEquals(12, invalid.getNumber());
        assertFalse(invalid.isComment());
        assertTrue(invalid.isEmpty());
        assertTrue(invalid.ignore());
    }
}