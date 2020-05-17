package tech.ikora.builder;

import org.junit.jupiter.api.Test;
import tech.ikora.Helpers;
import tech.ikora.error.ErrorManager;
import tech.ikora.exception.MalformedVariableException;
import tech.ikora.model.*;

import java.io.IOException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class VariableAssignmentParserTest {
    @Test
    void testParseScalarSimpleValue() throws IOException, MalformedVariableException {
        LineReader reader = Helpers.lineReader("${scalar}", "scalar value");

        ErrorManager errors = new ErrorManager();
        Optional<VariableAssignment> optional = VariableAssignmentParser.parse(reader, errors);
        assertTrue(optional.isPresent());
        VariableAssignment assignment = optional.get();
        assertTrue(errors.isEmpty());

        assertEquals(1, assignment.getValues().size());
        assertTrue(assignment.getValues().get(0) instanceof Literal);
        assertEquals("scalar value", assignment.getValues().get(0).getNameToken().toString());

        assertEquals(2, assignment.getTokens().size());
    }

    @Test
    void testParseScalarVariableValue() throws IOException, MalformedVariableException {
        LineReader reader = Helpers.lineReader("${scalar}", "@{variable_value}");

        ErrorManager errors = new ErrorManager();
        Optional<VariableAssignment> optional = VariableAssignmentParser.parse(reader, errors);
        assertTrue(optional.isPresent());
        VariableAssignment assignment = optional.get();
        assertTrue(errors.isEmpty());

        assertEquals(1, assignment.getValues().size());
        assertTrue(assignment.getValues().get(0) instanceof Variable);
        assertEquals("@{variable_value}", assignment.getValues().get(0).getNameToken().toString());
    }

    @Test
    void testParseScalarWithMultipleValue() throws IOException, MalformedVariableException {
        LineReader reader = Helpers.lineReader("${scalar}", "scalar value 1", "scalar value 2", "scalar value 3");

        ErrorManager errors = new ErrorManager();
        Optional<VariableAssignment> optional = VariableAssignmentParser.parse(reader, errors);
        assertTrue(optional.isPresent());
        VariableAssignment assignment = optional.get();
        assertFalse(errors.isEmpty());

        assertEquals(1, assignment.getValues().size());
        assertTrue(assignment.getValues().get(0) instanceof Literal);
        assertEquals("scalar value 1", assignment.getValues().get(0).getNameToken().toString());
    }

    @Test
    void testParseListWithMultipleValue() throws IOException, MalformedVariableException {
        LineReader reader = Helpers.lineReader("@{list}", "scalar value 1", "${variable}", "scalar value 3");

        ErrorManager errors = new ErrorManager();
        Optional<VariableAssignment> optional = VariableAssignmentParser.parse(reader, errors);
        assertTrue(optional.isPresent());
        VariableAssignment assignment = optional.get();
        assertTrue(errors.isEmpty());

        assertFalse(assignment.getValues().isEmpty());

        assertTrue(assignment.getValues().get(0) instanceof Literal);
        assertEquals("scalar value 1", assignment.getValues().get(0).getNameToken().toString());

        assertTrue(assignment.getValues().get(1) instanceof Variable);
        assertEquals("${variable}", assignment.getValues().get(1).getNameToken().toString());

        assertTrue(assignment.getValues().get(2) instanceof Literal);
        assertEquals("scalar value 3", assignment.getValues().get(2).getNameToken().toString());
    }

    @Test
    void testParseDictionaryWithMultipleEntry() throws IOException, MalformedVariableException {
        LineReader reader = Helpers.lineReader("&{dictionary}", "key1=value1", "${variable}", "key3=value3");

        ErrorManager errors = new ErrorManager();
        Optional<VariableAssignment> optional = VariableAssignmentParser.parse(reader, errors);
        assertTrue(optional.isPresent());
        VariableAssignment assignment = optional.get();
        assertTrue(errors.isEmpty());

        assertFalse(assignment.getValues().isEmpty());

        assertTrue(assignment.getValues().get(0) instanceof DictionaryEntry);
        assertEquals("key1", ((DictionaryEntry)assignment.getValues().get(0)).getKey().getNameToken().toString());
        assertEquals("value1", ((DictionaryEntry)assignment.getValues().get(0)).getValue().getNameToken().toString());

        assertTrue(assignment.getValues().get(1) instanceof Variable);
        assertEquals("${variable}", assignment.getValues().get(1).getNameToken().toString());

        assertTrue(assignment.getValues().get(2) instanceof DictionaryEntry);
        assertEquals("key3", ((DictionaryEntry)assignment.getValues().get(2)).getKey().getNameToken().toString());
        assertEquals("value3", ((DictionaryEntry)assignment.getValues().get(2)).getValue().getNameToken().toString());
    }
}
