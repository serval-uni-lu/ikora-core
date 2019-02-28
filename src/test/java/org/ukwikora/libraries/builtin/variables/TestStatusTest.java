package org.ukwikora.libraries.builtin.variables;

import org.junit.Test;

import static org.junit.Assert.*;

public class TestStatusTest {
    @Test
    public void checkVariableResolution(){
        TestStatus testStatus = new TestStatus();

        assertTrue(testStatus.matches("${TEST STATUS}"));
        assertTrue(testStatus.matches("${TEST_STATUS}"));
        assertTrue(testStatus.matches("${test status}"));

        assertFalse(testStatus.matches("${TEST CASE}"));
        assertFalse(testStatus.matches("&{TEST STATUS}"));
        assertFalse(testStatus.matches("TEST STATUS"));
    }
}