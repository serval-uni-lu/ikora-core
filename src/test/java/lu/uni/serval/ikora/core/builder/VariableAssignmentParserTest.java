package lu.uni.serval.ikora.core.builder;

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
import lu.uni.serval.ikora.core.model.VariableAssignment;
import org.junit.jupiter.api.Test;
import lu.uni.serval.ikora.core.Helpers;

import java.io.IOException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class VariableAssignmentParserTest {
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
        assertEquals("scalar value", assignment.getValues().get(0).getDefinitionToken().toString());

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

    @Test
    void testMultilineVariable() throws IOException {
        final String code = "${variable}    First Line\n" +
                "... Second Line\n" +
                "... Third Line\n";

        final LineReader reader = new LineReader(code);
        reader.readLine();

        ErrorManager errors = new ErrorManager();
        Optional<VariableAssignment> optional = VariableAssignmentParser.parse(reader, errors);
        assertTrue(optional.isPresent());
        VariableAssignment variableAssignment = optional.get();
        assertEquals(1, variableAssignment.getValues().size());
    }
}
