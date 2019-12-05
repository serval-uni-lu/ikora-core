package org.ikora.builder;

import org.junit.jupiter.api.Test;
import org.ikora.model.DictionaryVariable;
import org.ikora.model.ListVariable;
import org.ikora.model.ScalarVariable;
import org.ikora.model.Variable;

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