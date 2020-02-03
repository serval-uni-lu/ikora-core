package org.ikora.builder;

import org.ikora.error.ErrorManager;
import org.ikora.error.ErrorMessages;
import org.ikora.error.Errors;
import org.ikora.model.Assignment;
import org.ikora.model.KeywordCall;
import org.ikora.model.Variable;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class AssignmentParserTest {
    @Test
    void parseAssignmentFromKeywordToSingleVariable() throws IOException {
        String text = "\t${value}=\tKeyword returning a value";

        ErrorManager errors = new ErrorManager();
        Assignment assignment = createAssignment(text, errors);

        List<Variable> variables = assignment.getReturnVariables();
        assertEquals(1, variables.size());
        assertEquals("${value}", variables.get(0).getName().getText());

        Optional<KeywordCall> expression = assignment.getKeywordCall();
        assertTrue(expression.isPresent());
        assertEquals("Keyword returning a value", expression.get().getName().getText());
    }

    @Test
    void parseAssignmentFromKeywordToMultipleVariable() throws IOException {
        String text = "\t${value1}\t${value2}=\tKeyword returning a value";

        ErrorManager errors = new ErrorManager();
        Assignment assignment = createAssignment(text, errors);

        List<Variable> variables = assignment.getReturnVariables();
        assertEquals(2, variables.size());
        assertEquals("${value1}", variables.get(0).getName().getText());
        assertEquals("${value2}", variables.get(1).getName().getText());

        Optional<KeywordCall> expression = assignment.getKeywordCall();
        assertTrue(expression.isPresent());
        assertEquals("Keyword returning a value", expression.get().getName().getText());
    }

    @Test
    void parseAssignmentWithNothing() throws IOException {
        final String text = "\t${value}=";

        final ErrorManager errors = new ErrorManager();
        Assignment assignment = createAssignment(text, errors);

        final List<Variable> variables = assignment.getReturnVariables();
        assertEquals(1, variables.size());
        assertEquals("${value}", variables.get(0).getName().getText());

        final Optional<KeywordCall> expression = assignment.getKeywordCall();
        assertFalse(expression.isPresent());

        final Errors fileError = errors.in(new File("/"));
        assertEquals(1, fileError.getSize());
        assertEquals(ErrorMessages.ASSIGNMENT_SHOULD_HAVE_LEFT_HAND_OPERAND,
                fileError.getSyntaxErrors().iterator().next().getMessage());
    }

    private Assignment createAssignment(String text, ErrorManager errors) throws IOException {
        Reader targetReader = new StringReader(text);
        LineReader reader = new LineReader(targetReader);
        Tokens tokens = LexerUtils.tokenize(reader.readLine()).withoutIndent();

        return AssignmentParser.parse(reader, tokens, errors);
    }
}