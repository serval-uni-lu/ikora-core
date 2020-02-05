package tech.ikora.builder;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LineTest {
    @Test
    void checkSimpleLine(){
        Line simple = new Line("simple line", 4, false, false);

        assertTrue(simple.isValid());
        assertEquals("simple line", simple.getText());
        assertEquals(4, simple.getNumber());
        assertFalse(simple.isComment());
        assertFalse(simple.isEmpty());
        assertFalse(simple.ignore());
    }

    @Test
    void checkInvalidLine(){
        Line invalid = new Line(null, 3, false, true);

        assertFalse(invalid.isValid());
        assertNull(invalid.getText());
        assertEquals(3, invalid.getNumber());
        assertFalse(invalid.isComment());
        assertTrue(invalid.isEmpty());
        assertTrue(invalid.ignore());
    }

    @Test
    void checkEmptyLine(){
        Line invalid = new Line("", 12, false, true);

        assertTrue(invalid.isValid());
        assertEquals("", invalid.getText());
        assertEquals(12, invalid.getNumber());
        assertFalse(invalid.isComment());
        assertTrue(invalid.isEmpty());
        assertTrue(invalid.ignore());
    }
}