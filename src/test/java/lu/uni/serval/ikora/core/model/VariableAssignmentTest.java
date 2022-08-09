/*
 *
 *     Copyright © 2019 - 2022 University of Luxembourg
 *
 *     Licensed under the Apache License, Version 2.0 (the "License")
 *     you may not use this file except in compliance with the License.
 *     You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *     Unless required by applicable law or agreed to in writing, software
 *     distributed under the License is distributed on an "AS IS" BASIS,
 *     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *     See the License for the specific language governing permissions and
 *     limitations under the License.
 */
package lu.uni.serval.ikora.core.model;

import lu.uni.serval.ikora.core.analytics.difference.Edit;
import lu.uni.serval.ikora.core.exception.MalformedVariableException;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class VariableAssignmentTest {
    @Test
    void testAssignmentInitialization() throws MalformedVariableException {
        final VariableAssignment assignment = new VariableAssignment(Variable.create(Token.fromString("${variable}")));
        assignment.addValue(new Literal(Token.fromString("value")));

        assertEquals("${variable}", assignment.getName());
        assertEquals(1, assignment.getValues().size());
        assertEquals("value", assignment.getValues().get(0).getName());
    }

    @Test
    void testDifferenceByName() throws MalformedVariableException {
        final VariableAssignment assignment1 = new VariableAssignment(Variable.create(Token.fromString("${variable1}")));
        assignment1.addValue(new Literal(Token.fromString("value")));

        final VariableAssignment assignment2 = new VariableAssignment(Variable.create(Token.fromString("${variable2}")));
        assignment2.addValue(new Literal(Token.fromString("value")));

        final List<Edit> edits = assignment1.differences(assignment2);

        assertEquals(1, edits.size());
        assertEquals(1, edits.stream().filter(action -> action.getType() == Edit.Type.CHANGE_NAME).count());
    }

    @Test
    void testDifferenceByValue() throws MalformedVariableException {
        final VariableAssignment assignment1 = new VariableAssignment(Variable.create(Token.fromString("${variable}")));
        assignment1.addValue(new Literal(Token.fromString("value1")));

        final VariableAssignment assignment2 = new VariableAssignment(Variable.create(Token.fromString("${variable}")));
        assignment2.addValue(new Literal(Token.fromString("value2")));

        final List<Edit> edits = assignment1.differences(assignment2);

        assertEquals(1, edits.size());
        assertEquals(1, edits.stream().filter(action -> action.getType() == Edit.Type.CHANGE_VALUE_NAME).count());
    }

    @Test
    void testDifferenceByValueType() throws MalformedVariableException {
        final VariableAssignment assignment1 = new VariableAssignment(Variable.create(Token.fromString("${variable}")));
        assignment1.addValue(new Literal(Token.fromString("value")));

        final VariableAssignment assignment2 = new VariableAssignment(Variable.create(Token.fromString("${variable}")));
        assignment2.addValue(Variable.create(Token.fromString("${other}")));

        final List<Edit> edits = assignment1.differences(assignment2);

        assertEquals(1, edits.size());
        assertEquals(1, edits.stream().filter(action -> action.getType() == Edit.Type.CHANGE_VALUE_TYPE).count());
    }
}
