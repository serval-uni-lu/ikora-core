package lu.uni.serval.ikora.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ScalarVariableTest {
    @Test
    void testNameMatching(){
        ScalarVariable test1 = new ScalarVariable(Token.fromString("${test1}"));
        assertTrue(test1.matches(Token.fromString("${test1}")));
        assertTrue(test1.matches(Token.fromString("${TEST1}")));
        assertTrue(test1.matches(Token.fromString("${TEST 1}")));
        assertTrue(test1.matches(Token.fromString("${TEST_1}")));
        assertFalse(test1.matches(Token.fromString("&{test1}")));
        assertFalse(test1.matches(Token.fromString("@{test1}")));
    }

}