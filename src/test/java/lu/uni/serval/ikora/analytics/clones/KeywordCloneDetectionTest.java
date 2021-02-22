package lu.uni.serval.ikora.analytics.clones;

import lu.uni.serval.ikora.builder.BuildResult;
import lu.uni.serval.ikora.builder.Builder;
import lu.uni.serval.ikora.model.KeywordDefinition;
import lu.uni.serval.ikora.model.Project;
import lu.uni.serval.ikora.model.Token;
import lu.uni.serval.ikora.model.UserKeyword;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class KeywordCloneDetectionTest {
    private static UserKeyword keyword1;
    private static UserKeyword keyword2;
    private static UserKeyword keyword3;
    private static UserKeyword keyword4;
    private static UserKeyword keyword5;
    private static UserKeyword keyword6;
    private static UserKeyword keyword7;
    private static UserKeyword keyword8;

    private static Project project;

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

        project = result.getProjects().iterator().next();

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
    void testCloneDetectionInSimpleFile(){
        Clones<KeywordDefinition> clones = KeywordCloneDetection.findClones(project);

        assertEquals(2, clones.size(Clones.Type.TYPE_1));
        assertEquals(5, clones.size(Clones.Type.TYPE_2));
        assertEquals(7, clones.size(Clones.Type.TYPE_3));
    }

    @Test
    void testCloneFromOneself(){
        final Clones.Type type = KeywordCloneDetection.getCloneType(keyword1, keyword1);
        assertEquals(Clones.Type.TYPE_1, type);
    }

    @Test
    void testCloneType1(){
        final Clones.Type type = KeywordCloneDetection.getCloneType(keyword1, keyword2);
        assertEquals(Clones.Type.TYPE_1, type);
    }

    @Test
    void testCloneType2(){
        final Clones.Type type = KeywordCloneDetection.getCloneType(keyword1, keyword3);
        assertEquals(Clones.Type.TYPE_2, type);
    }

    @Test
    void testCloneType3(){
        final Clones.Type type = KeywordCloneDetection.getCloneType(keyword1, keyword4);
        assertEquals(Clones.Type.TYPE_3, type);
    }

    @Test
    void testCloneNone(){
        final Clones.Type type = KeywordCloneDetection.getCloneType(keyword1, keyword5);
        assertEquals(Clones.Type.NONE, type);
    }

    @Test
    void testCloneType2WithForLoop(){
        final Clones.Type type = KeywordCloneDetection.getCloneType(keyword6, keyword7);
        assertEquals(Clones.Type.TYPE_2, type);
    }

    @Test
    void testCloneType3WithForLoop(){
        final Clones.Type type = KeywordCloneDetection.getCloneType(keyword6, keyword8);
        assertEquals(Clones.Type.TYPE_3, type);
    }
}