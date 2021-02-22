package lu.uni.serval.ikora.libraries.builtin.variables;

import lu.uni.serval.ikora.model.Token;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TestStatusTest {
    @Test
    void checkVariableResolution(){
        TestStatus testStatus = new TestStatus();

        assertTrue(testStatus.matches(Token.fromString("${TEST STATUS}")));
        assertTrue(testStatus.matches(Token.fromString("${TEST_STATUS}")));
        assertTrue(testStatus.matches(Token.fromString("${test status}")));

        assertFalse(testStatus.matches(Token.fromString("${TEST CASE}")));
        assertFalse(testStatus.matches(Token.fromString("&{TEST STATUS}")));
        assertFalse(testStatus.matches(Token.fromString("TEST STATUS")));
    }
}