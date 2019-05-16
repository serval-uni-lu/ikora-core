package org.ukwikora.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DictionaryVariableTest {
    @Test
    void checkNameMatching(){
        DictionaryVariable test1 = new DictionaryVariable("&{test1}");
        assertTrue(test1.matches("&{test1}"));
        assertTrue(test1.matches("&{TEST1}"));
        assertFalse(test1.matches("${test1}"));
        assertFalse(test1.matches("@{test1}"));
    }
}