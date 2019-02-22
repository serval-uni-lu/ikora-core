package org.ukwikora.compiler;

import org.junit.Test;
import org.ukwikora.Globals;

import java.io.File;

import static org.junit.Assert.*;

public class LineReaderTest {
    @Test
    public void checkSpecialCharacterReading(){
        try {
            final File robot = Globals.getResourceFile("robot/special-characters.robot");
            LineReader reader = new LineReader(robot);

            Line line = reader.readLine();
            assertTrue(line.isValid());
            assertEquals("Line without special characters", line.getText());

            line = reader.readLine();
            assertTrue(line.isValid());
            assertEquals("Line with special characters: êôçàéèù!", line.getText());

            line = reader.readLine();
            assertFalse(line.isValid());

        } catch (Exception e) {
            fail(e.getMessage());
        }
    }
}