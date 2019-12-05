package org.ikora.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ListVariableTest {
    @Test
    void checkNameMatching(){
        ListVariable test1 = new ListVariable("@{test1}");
        assertTrue(test1.matches("@{test1}"));
        assertTrue(test1.matches("@{TEST1}"));
        assertTrue(test1.matches("@{TEST 1}"));
        assertTrue(test1.matches("@{TEST_1}"));
        assertTrue(test1.matches("${test1}"));
        assertTrue(test1.matches("@{test1[0]}"));
        assertTrue(test1.matches("@{test1[10]}"));
        assertTrue(test1.matches("@{test1[5][3]}"));
        assertFalse(test1.matches("&{test1}"));
        assertFalse(test1.matches("@{test1[char]}"));
    }
}