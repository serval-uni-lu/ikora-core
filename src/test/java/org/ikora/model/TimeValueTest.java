package org.ikora.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TimeValueTest {

    @Test
    void TestGetTimeInSeconds() {
        TimeValue time = new TimeValue(TimeValue.Sign.POSITIVE, 1, 1, 1, 1, 1);
        assertEquals(90061.001, time.getTimeInSeconds());
    }

    @Test
    void TestGetTimeInMilliseconds() {
        TimeValue time = new TimeValue(TimeValue.Sign.POSITIVE, 1, 1, 1, 1, 1);
        assertEquals(90061001, time.getTimeInMilliseconds());
    }

    @Test
    void TestIsValidWithNumeric() {
        assertTrue(TimeValue.isValid("10"));
        assertTrue(TimeValue.isValid("-1"));
        assertTrue(TimeValue.isValid("50.53"));
        assertTrue(TimeValue.isValid("-42.1"));
        assertTrue(TimeValue.isValid("0"));

        assertFalse(TimeValue.isValid("-10AB"));
    }

    @Test
    void TestIsValidWithString(){
        assertTrue(TimeValue.isValid("1 min 30 secs"));
        assertTrue(TimeValue.isValid("1.5 minutes"));
        assertTrue(TimeValue.isValid("90 s"));
        assertTrue(TimeValue.isValid("1 day 2 hours 3 minutes 4 seconds 5 milliseconds"));
        assertTrue(TimeValue.isValid("1d 2h 3m 4s 5ms"));
        assertTrue(TimeValue.isValid("- 10 seconds"));
    }

    @Test
    void TestIsValidWithTimer(){
        assertTrue(TimeValue.isValid("00:00:01"));
        assertTrue(TimeValue.isValid("01:02:03"));
        assertTrue(TimeValue.isValid("1:00:00"));
        assertTrue(TimeValue.isValid("100:00:00"));
        assertTrue(TimeValue.isValid("00:02"));
        assertTrue(TimeValue.isValid("42:00"));
        assertTrue(TimeValue.isValid("00:01:02.003"));
        assertTrue(TimeValue.isValid("00:01.5"));
        assertTrue(TimeValue.isValid("-01:02.345"));
    }
}