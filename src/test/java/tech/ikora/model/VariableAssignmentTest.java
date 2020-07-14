package tech.ikora.model;

import org.junit.jupiter.api.Test;
import tech.ikora.analytics.Action;
import tech.ikora.exception.MalformedVariableException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class VariableAssignmentTest {
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

        final List<Action> actions = assignment1.differences(assignment2);

        assertEquals(1, actions.size());
        assertEquals(1, actions.stream().filter(action -> action.getType() == Action.Type.CHANGE_NAME).count());
    }

    @Test
    void testDifferenceByValue() throws MalformedVariableException {
        final VariableAssignment assignment1 = new VariableAssignment(Variable.create(Token.fromString("${variable}")));
        assignment1.addValue(new Literal(Token.fromString("value1")));

        final VariableAssignment assignment2 = new VariableAssignment(Variable.create(Token.fromString("${variable}")));
        assignment2.addValue(new Literal(Token.fromString("value2")));

        final List<Action> actions = assignment1.differences(assignment2);

        assertEquals(1, actions.size());
        assertEquals(1, actions.stream().filter(action -> action.getType() == Action.Type.CHANGE_VALUE_NAME).count());
    }

    @Test
    void testDifferenceByValueType() throws MalformedVariableException {
        final VariableAssignment assignment1 = new VariableAssignment(Variable.create(Token.fromString("${variable}")));
        assignment1.addValue(new Literal(Token.fromString("value")));

        final VariableAssignment assignment2 = new VariableAssignment(Variable.create(Token.fromString("${variable}")));
        assignment2.addValue(Variable.create(Token.fromString("${other}")));

        final List<Action> actions = assignment1.differences(assignment2);

        assertEquals(1, actions.size());
        assertEquals(1, actions.stream().filter(action -> action.getType() == Action.Type.CHANGE_VALUE_TYPE).count());
    }
}
