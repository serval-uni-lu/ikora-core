package lu.uni.serval.ikora.core.analytics;

import lu.uni.serval.ikora.core.analytics.difference.Difference;
import lu.uni.serval.ikora.core.analytics.difference.Edit;
import lu.uni.serval.ikora.core.builder.BuildResult;
import lu.uni.serval.ikora.core.builder.Builder;
import lu.uni.serval.ikora.core.model.Project;
import lu.uni.serval.ikora.core.model.Token;
import lu.uni.serval.ikora.core.model.UserKeyword;
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
        final String code =
                "*** Keywords ***\n" +
                "\n" +
                "First keyword\n" +
                "    Open Browser  firefox  google.com\n" +
                "    Title Should be  google.com\n" +
                "    Close All Browsers\n" +
                "\n" +
                "Second keyword\n" +
                "    Open Browser  firefox  google.com\n" +
                "    Title Should be  google.com\n" +
                "    Close All Browsers\n" +
                "\n" +
                "Third keyword\n" +
                "    Open Browser  chrome  google.com\n" +
                "    Title Should be  google.com\n" +
                "    Close All Browsers\n" +
                "\n" +
                "Forth keyword\n" +
                "    Log  Opening browser\n" +
                "    Open Browser  firefox  google.com\n" +
                "    Title Should be  google.com\n" +
                "    Log  Closing browser\n" +
                "    Close All Browsers\n" +
                "\n" +
                "Fifth keyword\n" +
                "    Log  Opening browser\n" +
                "    ${result}=  Open Browser  firefox  google.com\n" +
                "    Title Should be  google.com\n" +
                "    Log  Closing browser\n" +
                "    Close All Browsers\n" +
                "\n" +
                "Sixth keyword\n" +
                "    :FOR    ${INDEX}    IN RANGE    1    3\n" +
                "    \\    Log    ${INDEX}\n" +
                "    \\    ${RANDOM_STRING}=    Generate Random String    ${INDEX}\n" +
                "    \\    Log    ${RANDOM_STRING}\n" +
                "\n" +
                "Seventh keyword\n" +
                "    :FOR    ${INDEX}    IN RANGE    1    10\n" +
                "    \\    Log    ${INDEX}\n" +
                "    \\    ${RANDOM_STRING}=    Generate Random String    ${INDEX}\n" +
                "    \\    Log    ${RANDOM_STRING}\n" +
                "\n" +
                "Eighth keyword\n" +
                "    :FOR    ${INDEX}    IN RANGE    1    3\n" +
                "    \\    ${RANDOM_STRING}=    Generate Random String    ${INDEX}\n" +
                "    \\    Log    ${RANDOM_STRING}";

        final BuildResult result = Builder.build(code, true);
        final Project project = result.getProjects().iterator().next();

        keyword1 = project.findUserKeyword(Token.fromString("First keyword")).iterator().next();
        keyword2 = project.findUserKeyword(Token.fromString("Second keyword")).iterator().next();
        keyword3 = project.findUserKeyword(Token.fromString("Third keyword")).iterator().next();
        keyword4 = project.findUserKeyword(Token.fromString("Forth keyword")).iterator().next();
        keyword5 = project.findUserKeyword(Token.fromString("Fifth keyword")).iterator().next();
        keyword6 = project.findUserKeyword(Token.fromString("Sixth keyword")).iterator().next();
        keyword7 = project.findUserKeyword(Token.fromString("Seventh keyword")).iterator().next();
        keyword8 = project.findUserKeyword(Token.fromString("Eighth keyword")).iterator().next();
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