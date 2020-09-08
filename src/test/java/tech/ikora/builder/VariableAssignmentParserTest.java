package tech.ikora.builder;

import org.junit.jupiter.api.Test;
import tech.ikora.Helpers;
import tech.ikora.error.ErrorManager;
import tech.ikora.model.*;

import java.io.IOException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class VariableAssignmentParserTest {
    @Test
    void testParseScalarSimpleValue() throws IOException {
        LineReader reader = Helpers.lineReader("${scalar}", "scalar value");

        ErrorManager errors = new ErrorManager();
        Optional<VariableAssignment> optional = VariableAssignmentParser.parse(reader, errors);
        assertTrue(optional.isPresent());
        VariableAssignment assignment = optional.get();
        assertTrue(errors.isEmpty());

        assertEquals(1, assignment.getValues().size());
        assertTrue(assignment.getValues().get(0).isLiteral());
        assertEquals("scalar value", assignment.getValues().get(0).getNameToken().toString());

        assertEquals(2, assignment.getTokens().size());
    }

    @Test
    void testParseScalarVariableValue() throws IOException {
        LineReader reader = Helpers.lineReader("${scalar}", "@{variable_value}");

        ErrorManager errors = new ErrorManager();
        Optional<VariableAssignment> optional = VariableAssignmentParser.parse(reader, errors);
        assertTrue(optional.isPresent());
        VariableAssignment assignment = optional.get();
        assertTrue(errors.isEmpty());

        assertEquals(1, assignment.getValues().size());
        assertTrue(assignment.getValues().get(0).isListVariable());
        assertEquals("@{variable_value}", assignment.getValues().get(0).toString());
    }

    @Test
    void testParseListWithMultipleValue() throws IOException {
        LineReader reader = Helpers.lineReader("@{list}", "scalar value 1", "${variable}", "scalar value 3");

        ErrorManager errors = new ErrorManager();
        Optional<VariableAssignment> optional = VariableAssignmentParser.parse(reader, errors);
        assertTrue(optional.isPresent());
        VariableAssignment assignment = optional.get();
        assertTrue(errors.isEmpty());

        assertFalse(assignment.getValues().isEmpty());

        assertTrue(assignment.getValues().get(0).isLiteral());
        assertEquals("scalar value 1", assignment.getValues().get(0).toString());

        assertTrue(assignment.getValues().get(1).isVariable());
        assertEquals("${variable}", assignment.getValues().get(1).toString());

        assertTrue(assignment.getValues().get(2).isLiteral());
        assertEquals("scalar value 3", assignment.getValues().get(2).toString());
    }

    @Test
    void testParseDictionaryWithMultipleEntry() throws IOException {
        LineReader reader = Helpers.lineReader("&{dictionary}", "key1=value1", "${variable}", "key3=value3");

        ErrorManager errors = new ErrorManager();
        Optional<VariableAssignment> optional = VariableAssignmentParser.parse(reader, errors);
        assertTrue(optional.isPresent());
        VariableAssignment assignment = optional.get();
        assertTrue(errors.isEmpty());

        assertFalse(assignment.getValues().isEmpty());

        assertTrue(assignment.getValues().get(0).isDictionaryEntry());
        assertEquals("key1", assignment.getValues().get(0).getName());

        assertTrue(assignment.getValues().get(1).isScalarVariable());
        assertEquals("${variable}", assignment.getValues().get(1).toString());

        assertTrue(assignment.getValues().get(2).isDictionaryEntry());
        assertEquals("key3", assignment.getValues().get(2).toString());
    }
}
