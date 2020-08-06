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
    private static UserKeyword keyword6;
    private static UserKeyword keyword7;
    private static UserKeyword keyword8;

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
        assertNotNull(keyword5);

        keyword6 = project.findUserKeyword(Token.fromString("Sixth keyword")).iterator().next();
        assertNotNull(keyword6);

        keyword7 = project.findUserKeyword(Token.fromString("Seventh keyword")).iterator().next();
        assertNotNull(keyword7);

        keyword8 = project.findUserKeyword(Token.fromString("Eighth keyword")).iterator().next();
        assertNotNull(keyword8);
    }

    @Test
    void testDifferenceWithOneselfIsEmpty(){
        Difference difference = Difference.of(keyword1, keyword1);
        assertTrue(difference.isEmpty());
    }

    @Test
    void testDifferenceKeywordOnlyNameChange(){
        Difference difference = Difference.of(keyword1, keyword2);
        List<Edit> edits = difference.getEdits();

        assertEquals(1, edits.size());
        assertEquals(1, edits.stream().filter(edit -> edit.getType() == Edit.Type.CHANGE_NAME).count());
    }

    @Test
    void testDifferenceKeywordChangeLiteralArgument(){
        Difference difference = Difference.of(keyword2, keyword3);
        List<Edit> edits = difference.getEdits();

        assertEquals(2, edits.size());
        assertEquals(1, edits.stream().filter(edit -> edit.getType() == Edit.Type.CHANGE_NAME).count());
        assertEquals(1, edits.stream().filter(edit -> edit.getType() == Edit.Type.CHANGE_VALUE_NAME).count());
    }

    @Test
    void testDifferenceKeywordAddAndModifySteps(){
        Difference difference = Difference.of(keyword3, keyword4);
        List<Edit> edits = difference.getEdits();

        assertEquals(4, edits.size());
        assertEquals(1, edits.stream().filter(edit -> edit.getType() == Edit.Type.CHANGE_NAME).count());
        assertEquals(2, edits.stream().filter(edit -> edit.getType() == Edit.Type.ADD_STEP).count());
        assertEquals(1, edits.stream().filter(edit -> edit.getType() == Edit.Type.CHANGE_VALUE_NAME).count());
    }

    @Test
    void testFromCallToAssignment(){
        Difference difference = Difference.of(keyword4, keyword5);
        List<Edit> edits = difference.getEdits();

        assertEquals(2, edits.size());
        assertEquals(1, edits.stream().filter(edit -> edit.getType() == Edit.Type.CHANGE_NAME).count());
        assertEquals(1, edits.stream().filter(edit -> edit.getType() == Edit.Type.ADD_VARIABLE).count());
    }

    @Test
    void testFromAssignmentToCall(){
        Difference difference = Difference.of(keyword5, keyword4);
        List<Edit> edits = difference.getEdits();

        assertEquals(2, edits.size());
        assertEquals(1, edits.stream().filter(edit -> edit.getType() == Edit.Type.CHANGE_NAME).count());
        assertEquals(1, edits.stream().filter(edit -> edit.getType() == Edit.Type.REMOVE_VARIABLE).count());
    }

    @Test
    void testForLoopCondition(){
        Difference difference = Difference.of(keyword6, keyword7);
        List<Edit> edits = difference.getEdits();

        assertEquals(2, edits.size());
        assertEquals(1, edits.stream().filter(edit -> edit.getType() == Edit.Type.CHANGE_NAME).count());
        assertEquals(1, edits.stream().filter(edit -> edit.getType() == Edit.Type.CHANGE_VALUE_NAME).count());
    }

    @Test
    void testForLoopInBody(){
        Difference difference = Difference.of(keyword6, keyword8);
        List<Edit> edits = difference.getEdits();

        assertEquals(2, edits.size());
        assertEquals(1, edits.stream().filter(edit -> edit.getType() == Edit.Type.CHANGE_NAME).count());
        assertEquals(1, edits.stream().filter(edit -> edit.getType() == Edit.Type.REMOVE_STEP).count());
    }
}