package org.ikora.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ScalarVariableTest {
    @Test
    void checkNameMatching(){
        ScalarVariable test1 = new ScalarVariable("${test1}");
        assertTrue(test1.matches("${test1}"));
        assertTrue(test1.matches("${TEST1}"));
        assertTrue(test1.matches("${TEST 1}"));
        assertTrue(test1.matches("${TEST_1}"));
        assertFalse(test1.matches("&{test1}"));
        assertFalse(test1.matches("@{test1}"));
    }

}