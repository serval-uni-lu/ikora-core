package tech.ikora.builder;

import org.junit.jupiter.api.Test;
import tech.ikora.Helpers;
import tech.ikora.error.ErrorManager;
import tech.ikora.exception.MalformedVariableException;
import tech.ikora.model.*;

import java.io.IOException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class VariableParserTest {
    @Test
    void testParseScalarName(){
        Token scalar = Token.fromString("${scalar}");
        Optional<Variable> variable = VariableParser.parseName(scalar);
        assertTrue(variable.isPresent());
        assertEquals(variable.get().getClass(), ScalarVariable.class);
    }

    @Test
    void testParseListName(){
        Token list = Token.fromString("@{list}");
        Optional<Variable> variable = VariableParser.parseName(list);
        assertTrue(variable.isPresent());
        assertEquals(variable.get().getClass(), ListVariable.class);
    }

    @Test
    void testParseDictionaryName(){
        Token dictionary = Token.fromString("&{dictionary}");
        Optional<Variable> variable = VariableParser.parseName(dictionary);
        assertTrue(variable.isPresent());
        assertEquals(variable.get().getClass(), DictionaryVariable.class);
    }

    @Test
    void testParseNonVariableName(){
        Token random = Token.fromString("not a variable");
        Optional<Variable> variable = VariableParser.parseName(random);
        assertFalse(variable.isPresent());
    }

    @Test
    void testParseNonVariableContainingAVariable(){
        Token random = Token.fromString("not a variable containing a ${scalar}");
        Optional<Variable> variable = VariableParser.parseName(random);
        assertFalse(variable.isPresent());
    }

    @Test
    void testParseScalarSimpleValue() throws IOException, MalformedVariableException {
        LineReader reader = Helpers.lineReader("scalar value");
        ScalarVariable variable = (ScalarVariable) Variable.create(Token.fromString("${scalar}"));

        ErrorManager errors = new ErrorManager();
        VariableParser.parseValues(variable, LexerUtils.tokenize(reader), reader, errors);
        assertTrue(errors.isEmpty());

        assertTrue(variable.getValue().isPresent());
        assertTrue(variable.getValue().get() instanceof Literal);
        assertEquals("scalar value", variable.getValue().get().getName().toString());
    }

    @Test
    void testParseScalarVariableValue() throws IOException, MalformedVariableException {
        LineReader reader = Helpers.lineReader("@{variable_value}");
        Variable variable = Variable.create(Token.fromString("${scalar}"));

        ErrorManager errors = new ErrorManager();
        VariableParser.parseValues(variable, LexerUtils.tokenize(reader), reader, errors);
        assertTrue(errors.isEmpty());

        assertEquals(1, variable.getValues().size());
        assertTrue(variable.getValues().get(0) instanceof Variable);
        assertEquals("@{variable_value}", variable.getValues().get(0).getName().toString());
    }

    @Test
    void testParseScalarWithMultipleValue() throws IOException, MalformedVariableException {
        LineReader reader = Helpers.lineReader("scalar value 1", "scalar value 2", "scalar value 3");
        Variable variable = Variable.create(Token.fromString("${scalar}"));

        ErrorManager errors = new ErrorManager();
        VariableParser.parseValues(variable, LexerUtils.tokenize(reader), reader, errors);
        assertFalse(errors.isEmpty());

        assertEquals(1, variable.getValues().size());
        assertTrue(variable.getValues().get(0) instanceof Literal);
        assertEquals("scalar value 1", variable.getValues().get(0).getName().toString());
    }

    @Test
    void testParseListWithMultipleValue() throws IOException, MalformedVariableException {
        LineReader reader = Helpers.lineReader("scalar value 1", "${variable}", "scalar value 3");
        Variable variable = Variable.create(Token.fromString("@{list}"));

        ErrorManager errors = new ErrorManager();
        VariableParser.parseValues(variable, LexerUtils.tokenize(reader), reader, errors);
        assertTrue(errors.isEmpty());

        assertFalse(variable.getValues().isEmpty());

        assertTrue(variable.getValues().get(0) instanceof Literal);
        assertEquals("scalar value 1", variable.getValues().get(0).getName().toString());

        assertTrue(variable.getValues().get(1) instanceof Variable);
        assertEquals("${variable}", variable.getValues().get(1).getName().toString());

        assertTrue(variable.getValues().get(2) instanceof Literal);
        assertEquals("scalar value 3", variable.getValues().get(2).getName().toString());
    }

    @Test
    void testParseDictionaryWithMultipleEntry() throws IOException, MalformedVariableException {
        LineReader reader = Helpers.lineReader("key1=value1", "${variable}", "key3=value3");
        Variable variable = Variable.create(Token.fromString("&{dictionary}"));

        ErrorManager errors = new ErrorManager();
        VariableParser.parseValues(variable, LexerUtils.tokenize(reader), reader, errors);
        assertTrue(errors.isEmpty());

        assertFalse(variable.getValues().isEmpty());

        assertTrue(variable.getValues().get(0) instanceof DictionaryEntry);
        assertEquals("key1", ((DictionaryEntry)variable.getValues().get(0)).getKey().getName().toString());
        assertEquals("value1", ((DictionaryEntry)variable.getValues().get(0)).getValue().getName().toString());

        assertTrue(variable.getValues().get(1) instanceof Variable);
        assertEquals("${variable}", variable.getValues().get(1).getName().toString());

        assertTrue(variable.getValues().get(2) instanceof DictionaryEntry);
        assertEquals("key3", ((DictionaryEntry)variable.getValues().get(2)).getKey().getName().toString());
        assertEquals("value3", ((DictionaryEntry)variable.getValues().get(2)).getValue().getName().toString());
    }
}