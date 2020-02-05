package tech.ikora.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ListVariableTest {
    @Test
    void checkNameMatching(){
        ListVariable test1 = new ListVariable(Token.fromString("@{test1}"));
        assertTrue(test1.matches(Token.fromString("@{test1}")));
        assertTrue(test1.matches(Token.fromString("@{TEST1}")));
        assertTrue(test1.matches(Token.fromString("@{TEST 1}")));
        assertTrue(test1.matches(Token.fromString("@{TEST_1}")));
        assertTrue(test1.matches(Token.fromString("${test1}")));
        assertTrue(test1.matches(Token.fromString("@{test1[0]}")));
        assertTrue(test1.matches(Token.fromString("@{test1[10]}")));
        assertTrue(test1.matches(Token.fromString("@{test1[5][3]}")));
        assertFalse(test1.matches(Token.fromString("&{test1}")));
        assertFalse(test1.matches(Token.fromString("@{test1[char]}")));
    }
}