package org.ukwikora.report;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

import static org.junit.jupiter.api.Assertions.*;

class UtilsTest {
    @Test
    void checkToLocalDateTimeWithValidDate(){
        assertEquals(LocalDateTime.of(2017,2,20,14,19,7,693000000), Utils.toLocalDateTime("20170220 14:19:07.693"));
    }

    @Test
    void checkToLocalDateTimeWithInvalidDate(){
        try{
            Utils.toLocalDateTime("Invalid date");
        } catch (DateTimeParseException e){
            assertTrue(e.getMessage().contains("Text 'Invalid date' could not be parsed"));
        }
    }

    @Test
    void checkToBooleanWithValidInput(){
        try {
            assertTrue(Utils.toBoolean("TRUE"));
            assertTrue(Utils.toBoolean("True"));
            assertTrue(Utils.toBoolean("true"));
            assertTrue(Utils.toBoolean("YES"));
            assertTrue(Utils.toBoolean("Yes"));
            assertTrue(Utils.toBoolean("yes"));

            assertFalse(Utils.toBoolean("FALSE"));
            assertFalse(Utils.toBoolean("False"));
            assertFalse(Utils.toBoolean("false"));
            assertFalse(Utils.toBoolean("NO"));
            assertFalse(Utils.toBoolean("No"));
            assertFalse(Utils.toBoolean("no"));
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

    @Test
    void checkToBooleanWithInvalidInput(){
        try{
            Utils.toBoolean("Invalid");
        } catch (IOException e){
            assertTrue(e.getMessage().contains("Could not convert 'Invalid' to boolean value"));
        }
    }
}