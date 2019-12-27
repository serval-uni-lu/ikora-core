package org.ikora.analytics;

import org.ikora.Helpers;
import org.ikora.model.Project;
import org.ikora.model.UserKeyword;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DifferenceTest {
    private static UserKeyword keyword1;
    private static UserKeyword keyword2;
    private static UserKeyword keyword3;
    private static UserKeyword keyword4;

    @BeforeAll
    static void setUp() {
        Project project = Helpers.compileProject("robot/clones.robot", true);
        keyword1 = project.findUserKeyword("First keyword").iterator().next();
        assertNotNull(keyword1);

        keyword2 = project.findUserKeyword("Second keyword").iterator().next();
        assertNotNull(keyword2);

        keyword3 = project.findUserKeyword("Third keyword").iterator().next();
        assertNotNull(keyword3);

        keyword4 = project.findUserKeyword("Forth keyword").iterator().next();
        assertNotNull(keyword4);
    }

    @Test
    void testDifferenceWithOneselfIsEmpty(){
        Difference difference = Difference.of(keyword1, keyword1);
        assertTrue(difference.isEmpty());
    }

    @Test
    void testDifferenceKeywordOnlyNameChange(){
        Difference difference = Difference.of(keyword1, keyword2);
        List<Action> actions = difference.getActions();

        assertEquals(1, actions.size());
        assertEquals(1, actions.stream().filter(action -> action.getType() == Action.Type.CHANGE_NAME).count());
    }

    @Test
    void testDifferenceKeywordAddAndModifySteps(){
        Difference difference = Difference.of(keyword3, keyword4);
        List<Action> actions = difference.getActions();

        // I will make that test fail because there is something shady in the action list
        assertEquals(4, actions.size());
        assertEquals(1, actions.stream().filter(action -> action.getType() == Action.Type.CHANGE_NAME).count());
        assertEquals(2, actions.stream().filter(action -> action.getType() == Action.Type.ADD_STEP).count());
        assertEquals(1, actions.stream().filter(action -> action.getType() == Action.Type.CHANGE_STEP_ARGUMENTS).count());
    }
}