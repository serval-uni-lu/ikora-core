package org.ikora.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DictionaryVariableTest {
    @Test
    void checkNameMatching(){
        DictionaryVariable test1 = new DictionaryVariable("&{test1}");
        assertTrue(test1.matches("&{test1}"));
        assertTrue(test1.matches("&{TEST1}"));
        assertTrue(test1.matches("&{TEST_1}"));
        assertTrue(test1.matches("&{TEST 1}"));
        assertFalse(test1.matches("${test1}"));
        assertFalse(test1.matches("@{test1}"));
    }
}