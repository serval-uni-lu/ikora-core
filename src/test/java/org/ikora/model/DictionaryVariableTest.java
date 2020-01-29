package org.ikora.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DictionaryVariableTest {
    @Test
    void checkNameMatching(){
        DictionaryVariable test1 = new DictionaryVariable(Token.fromString("&{test1}"));
        assertTrue(test1.matches(Token.fromString("&{test1}")));
        assertTrue(test1.matches(Token.fromString("&{TEST1}")));
        assertTrue(test1.matches(Token.fromString("&{TEST_1}")));
        assertTrue(test1.matches(Token.fromString("&{TEST 1}")));
        assertFalse(test1.matches(Token.fromString("${test1}")));
        assertFalse(test1.matches(Token.fromString("@{test1}")));
    }
}