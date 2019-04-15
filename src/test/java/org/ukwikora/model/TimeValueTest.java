package org.ukwikora.model;

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
}