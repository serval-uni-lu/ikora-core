package lu.uni.serval.ikora.core.parser;

/*-
 * #%L
 * Ikora Core
 * %%
 * Copyright (C) 2019 - 2021 University of Luxembourg
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License")
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import lu.uni.serval.ikora.core.error.ErrorManager;
import lu.uni.serval.ikora.core.model.Token;
import lu.uni.serval.ikora.core.model.VariableAssignment;
import org.junit.jupiter.api.Test;
import lu.uni.serval.ikora.core.Helpers;

import java.io.IOException;
import java.util.Iterator;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class VariableAssignmentParserTest {
    @Test
    void testParseScalarSimpleValue() throws IOException {
        final String text = "${scalar}\tscalar value";
        final ErrorManager errors = new ErrorManager();
        final Optional<VariableAssignment> variableAssignment = createVariableAssignment(text, errors);

        assertTrue(variableAssignment.isPresent());
        VariableAssignment assignment = variableAssignment.get();
        assertTrue(errors.isEmpty());

        assertEquals(1, assignment.getValues().size());
        assertTrue(assignment.getValues().get(0).isLiteral());
        assertEquals("scalar value", assignment.getValues().get(0).getDefinitionToken().toString());

        assertEquals(2, assignment.getTokens().size());
    }

    @Test
    void testParseScalarVariableValue() throws IOException {
        final String text = "${scalar}\t@{variable_value}";
        final ErrorManager errors = new ErrorManager();
        final Optional<VariableAssignment> variableAssignment = createVariableAssignment(text, errors);

        assertTrue(variableAssignment.isPresent());
        VariableAssignment assignment = variableAssignment.get();
        assertTrue(errors.isEmpty());

        assertEquals(1, assignment.getValues().size());
        assertTrue(assignment.getValues().get(0).isListVariable());
        assertEquals("@{variable_value}", assignment.getValues().get(0).toString());
    }

    @Test
    void testParseListWithMultipleValue() throws IOException {
        final String text = "@{list}\tscalar value 1\t${variable}\tscalar value 3";
        final ErrorManager errors = new ErrorManager();
        final Optional<VariableAssignment> variableAssignment = createVariableAssignment(text, errors);

        assertTrue(variableAssignment.isPresent());
        VariableAssignment assignment = variableAssignment.get();
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
        final String text = "&{dictionary}\tkey1=value1\t${variable}\tkey3=value3";
        final ErrorManager errors = new ErrorManager();
        final Optional<VariableAssignment> variableAssignment = createVariableAssignment(text, errors);

        assertTrue(variableAssignment.isPresent());
        VariableAssignment assignment = variableAssignment.get();
        assertTrue(errors.isEmpty());

        assertFalse(assignment.getValues().isEmpty());

        assertTrue(assignment.getValues().get(0).isDictionaryEntry());
        assertEquals("key1", assignment.getValues().get(0).getName());

        assertTrue(assignment.getValues().get(1).isScalarVariable());
        assertEquals("${variable}", assignment.getValues().get(1).toString());

        assertTrue(assignment.getValues().get(2).isDictionaryEntry());
        assertEquals("key3", assignment.getValues().get(2).toString());
    }

    @Test
    void testMultilineVariable() throws IOException {
        final String text = "${variable}    First Line\n" +
                "... Second Line\n" +
                "... Third Line\n";

        final ErrorManager errors = new ErrorManager();
        final Optional<VariableAssignment> variableAssignment = createVariableAssignment(text, errors);

        assertTrue(variableAssignment.isPresent());
        assertEquals(1, variableAssignment.get().getValues().size());
    }

    private Optional<VariableAssignment> createVariableAssignment(String text, ErrorManager errors) throws IOException {
        final LineReader reader = Helpers.getLineReader(text);
        final Iterator<Token> tokenIterator = TokenScanner.from(LexerUtils.tokenize(reader))
                .skipTypes(Token.Type.CONTINUATION)
                .iterator();

        return VariableAssignmentParser.parse(reader, tokenIterator, errors);
    }
}
