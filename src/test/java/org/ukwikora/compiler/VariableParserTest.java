package org.ukwikora.compiler;

import org.junit.jupiter.api.Test;
import org.ukwikora.model.DictionaryVariable;
import org.ukwikora.model.ListVariable;
import org.ukwikora.model.ScalarVariable;
import org.ukwikora.model.Variable;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class VariableParserTest {
    @Test
    void parseScalar(){
        String scalar = "${scalar}";
        Optional<Variable> variable = VariableParser.parse(scalar);
        assertTrue(variable.isPresent());
        assertEquals(variable.get().getClass(), ScalarVariable.class);
    }

    @Test
    void parseList(){
        String list = "@{list}";
        Optional<Variable> variable = VariableParser.parse(list);
        assertTrue(variable.isPresent());
        assertEquals(variable.get().getClass(), ListVariable.class);
    }

    @Test
    void parseDictionary(){
        String dictionary = "&{dictionary}";
        Optional<Variable> variable = VariableParser.parse(dictionary);
        assertTrue(variable.isPresent());
        assertEquals(variable.get().getClass(), DictionaryVariable.class);
    }

    @Test
    void parseNonVariable(){
        String random = "not a variable";
        Optional<Variable> variable = VariableParser.parse(random);
        assertFalse(variable.isPresent());
    }

    @Test
    void parseNonVariableContainingAVariable(){
        String random = "not a variable containing a ${scalar}";
        Optional<Variable> variable = VariableParser.parse(random);
        assertFalse(variable.isPresent());
    }
}