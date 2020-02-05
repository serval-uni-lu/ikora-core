package tech.ikora.report;

import org.junit.jupiter.api.Test;

import java.sql.Date;
import java.text.ParseException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

import static org.junit.jupiter.api.Assertions.*;

class ConverterTest {
    @Test
    void checkToLocalDateTimeWithValidDate(){
        try {
            final Instant instant = LocalDateTime.of(2017, 2, 20, 14, 19, 7, 693000000)
                    .atZone(ZoneId.systemDefault()).toInstant();

            assertEquals(Date.from(instant), Converter.toDate("20170220 14:19:07.693"));
        } catch (ParseException e) {
            fail(e.getMessage());
        }
    }

    @Test
    void checkToLocalDateTimeWithInvalidDate(){
        try{
            Converter.toDate("Invalid date");
        } catch (ParseException e){
            assertTrue(e.getMessage().contains("Unparseable date: \"Invalid date\""));
        }
    }

    @Test
    void checkToBooleanWithValidInput(){
        try {
            assertTrue(Converter.toBoolean("TRUE"));
            assertTrue(Converter.toBoolean("True"));
            assertTrue(Converter.toBoolean("true"));
            assertTrue(Converter.toBoolean("YES"));
            assertTrue(Converter.toBoolean("Yes"));
            assertTrue(Converter.toBoolean("yes"));

            assertFalse(Converter.toBoolean("FALSE"));
            assertFalse(Converter.toBoolean("False"));
            assertFalse(Converter.toBoolean("false"));
            assertFalse(Converter.toBoolean("NO"));
            assertFalse(Converter.toBoolean("No"));
            assertFalse(Converter.toBoolean("no"));
        } catch (ParseException e) {
            fail(e.getMessage());
        }
    }

    @Test
    void checkToBooleanWithInvalidInput(){
        try{
            Converter.toBoolean("Invalid");
        } catch (ParseException e){
            assertTrue(e.getMessage().contains("Could not convert 'Invalid' to boolean value"));
        }
    }
}