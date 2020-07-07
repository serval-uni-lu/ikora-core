package tech.ikora.builder;

import tech.ikora.Helpers;
import tech.ikora.error.ErrorManager;
import tech.ikora.error.ErrorMessages;
import tech.ikora.error.Errors;
import tech.ikora.model.*;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class AssignmentParserTest {
    @Test
    void parseAssignmentFromKeywordToSingleVariable() throws IOException {
        String text = "\t${value}=\tKeyword returning a value";

        ErrorManager errors = new ErrorManager();
        Assignment assignment = createAssignment(text, errors);

        List<Variable> variables = assignment.getLeftHandOperand();
        assertEquals(1, variables.size());
        assertEquals("${value}", variables.get(0).getName());

        Optional<KeywordCall> expression = assignment.getKeywordCall();
        assertTrue(expression.isPresent());
        assertEquals("Keyword returning a value", expression.get().getName());
    }

    @Test
    void parseAssignmentFromKeywordToMultipleVariable() throws IOException {
        String text = "\t${value1}\t${value2}=\tKeyword returning a value";

        ErrorManager errors = new ErrorManager();
        Assignment assignment = createAssignment(text, errors);

        List<Variable> variables = assignment.getLeftHandOperand();
        assertEquals(2, variables.size());
        assertEquals("${value1}", variables.get(0).getName());
        assertEquals("${value2}", variables.get(1).getName());

        Optional<KeywordCall> expression = assignment.getKeywordCall();
        assertTrue(expression.isPresent());
        assertEquals("Keyword returning a value", expression.get().getName());
    }

    @Test
    void parseAssignmentWithNothing() throws IOException {
        final String text = "\t${value}=";

        final ErrorManager errors = new ErrorManager();
        Assignment assignment = createAssignment(text, errors);

        final List<Variable> variables = assignment.getLeftHandOperand();
        assertEquals(1, variables.size());
        assertEquals("${value}", variables.get(0).getName());

        final Optional<KeywordCall> expression = assignment.getKeywordCall();
        assertFalse(expression.isPresent());

        final Errors fileError = errors.inMemory();
        assertEquals(1, fileError.getSize());
        assertEquals(ErrorMessages.ASSIGNMENT_SHOULD_HAVE_LEFT_HAND_OPERAND,
                fileError.getSyntaxErrors().iterator().next().getMessage());
    }

    private Assignment createAssignment(String text, ErrorManager errors) throws IOException {
        LineReader reader = Helpers.lineReader(text);
        Tokens tokens = LexerUtils.tokenize(reader).withoutIndent();

        return AssignmentParser.parse(reader, tokens, errors);
    }
}