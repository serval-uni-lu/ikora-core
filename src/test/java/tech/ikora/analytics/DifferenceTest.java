package tech.ikora.analytics;

import tech.ikora.Helpers;
import tech.ikora.model.Project;
import tech.ikora.model.Token;
import tech.ikora.model.UserKeyword;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DifferenceTest {
    private static UserKeyword keyword1;
    private static UserKeyword keyword2;
    private static UserKeyword keyword3;
    private static UserKeyword keyword4;
    private static UserKeyword keyword5;

    @BeforeAll
    static void setUp() {
        Project project = Helpers.compileProject("robot/clones.robot", true);
        keyword1 = project.findUserKeyword(Token.fromString("First keyword")).iterator().next();
        assertNotNull(keyword1);

        keyword2 = project.findUserKeyword(Token.fromString("Second keyword")).iterator().next();
        assertNotNull(keyword2);

        keyword3 = project.findUserKeyword(Token.fromString("Third keyword")).iterator().next();
        assertNotNull(keyword3);

        keyword4 = project.findUserKeyword(Token.fromString("Forth keyword")).iterator().next();
        assertNotNull(keyword4);

        keyword5 = project.findUserKeyword(Token.fromString("Fifth keyword")).iterator().next();
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

        assertEquals(4, actions.size());
        assertEquals(1, actions.stream().filter(action -> action.getType() == Action.Type.CHANGE_NAME).count());
        assertEquals(2, actions.stream().filter(action -> action.getType() == Action.Type.ADD_STEP).count());
        assertEquals(1, actions.stream().filter(action -> action.getType() == Action.Type.CHANGE_STEP_ARGUMENT).count());
    }

    @Test
    void testFromCallToAssignment(){
        Difference difference = Difference.of(keyword4, keyword5);
        List<Action> actions = difference.getActions();

        assertEquals(2, actions.size());
        assertEquals(1, actions.stream().filter(action -> action.getType() == Action.Type.ADD_VARIABLE).count());
    }

    @Test
    void testFromAssignmentToCall(){
        Difference difference = Difference.of(keyword5, keyword4);
        List<Action> actions = difference.getActions();

        assertEquals(2, actions.size());
        assertEquals(1, actions.stream().filter(action -> action.getType() == Action.Type.REMOVE_VARIABLE).count());
    }
}