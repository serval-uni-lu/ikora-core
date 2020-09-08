package tech.ikora.builder;

import org.junit.jupiter.api.Test;
import tech.ikora.model.*;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class VariableParserTest {
    @Test
    void testParseScalarName(){
        Token scalar = Token.fromString("${scalar}");
        Optional<Variable> variable = VariableParser.parse(scalar);
        assertTrue(variable.isPresent());
        assertEquals(variable.get().getClass(), ScalarVariable.class);
    }

    @Test
    void testParseListName(){
        Token list = Token.fromString("@{list}");
        Optional<Variable> variable = VariableParser.parse(list);
        assertTrue(variable.isPresent());
        assertEquals(variable.get().getClass(), ListVariable.class);
    }

    @Test
    void testParseDictionaryName(){
        Token dictionary = Token.fromString("&{dictionary}");
        Optional<Variable> variable = VariableParser.parse(dictionary);
        assertTrue(variable.isPresent());
        assertEquals(variable.get().getClass(), DictionaryVariable.class);
    }

    @Test
    void testParseNonVariableName(){
        Token random = Token.fromString("not a variable");
        Optional<Variable> variable = VariableParser.parse(random);
        assertFalse(variable.isPresent());
    }

    @Test
    void testParseNonVariableContainingAVariable(){
        Token random = Token.fromString("not a variable containing a ${scalar}");
        Optional<Variable> variable = VariableParser.parse(random);
        assertFalse(variable.isPresent());
    }
}