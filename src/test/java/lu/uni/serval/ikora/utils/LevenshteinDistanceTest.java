package lu.uni.serval.ikora.utils;

import lu.uni.serval.ikora.builder.BuildResult;
import lu.uni.serval.ikora.builder.Builder;
import lu.uni.serval.ikora.model.Project;
import lu.uni.serval.ikora.model.Step;
import lu.uni.serval.ikora.model.Token;
import lu.uni.serval.ikora.model.UserKeyword;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import lu.uni.serval.ikora.Helpers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LevenshteinDistanceTest {
    private static UserKeyword keyword1;
    private static UserKeyword keyword2;
    private static UserKeyword keyword3;

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
                        "    Log  Opening browser\n" +
                        "    Open Browser  firefox  google.com\n" +
                        "    Title Should be  google.com\n" +
                        "    Log  Closing browser\n" +
                        "    Close All Browsers\n" +
                        "\n";

        final BuildResult result = Builder.build(code, true);

        project = result.getProjects().iterator().next();

        keyword1 = project.findUserKeyword(Token.fromString("First keyword")).iterator().next();
        keyword2 = project.findUserKeyword(Token.fromString("Second keyword")).iterator().next();
        keyword3 = project.findUserKeyword(Token.fromString("Third keyword")).iterator().next();
    }


    @Test
    void testLevenshteinDistanceDifferentString() {
        String string1 = "First String";
        String string2 = "Second String";

        int distance = LevenshteinDistance.stringDistance(string1, string2);

        assertEquals(6, distance);
    }

    @Test
    void testLevenshteinIndexDifferentString() {
        String string1 = "First String";
        String string2 = "Second String";

        double index = LevenshteinDistance.stringIndex(string1, string2);

        Assertions.assertEquals(0.461538461538, index, Helpers.epsilon);
    }

    @Test
    void testKeywordStepsMappingSame(){
        List<Pair<Step, Step>> mapping = LevenshteinDistance.getMapping(keyword1.getSteps(), keyword2.getSteps());
        assertEquals(3, mapping.size());
    }

    @Test
    void testKeywordStepsMappingWithExtraStatements(){
        List<Pair<Step, Step>> mapping = LevenshteinDistance.getMapping(keyword1.getSteps(), keyword3.getSteps());
        assertEquals(3, mapping.size());
    }
}
