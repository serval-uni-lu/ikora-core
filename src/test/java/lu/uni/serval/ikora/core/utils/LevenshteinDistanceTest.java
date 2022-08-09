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
package lu.uni.serval.ikora.core.utils;

import lu.uni.serval.ikora.core.builder.BuildResult;
import lu.uni.serval.ikora.core.builder.Builder;
import lu.uni.serval.ikora.core.model.Project;
import lu.uni.serval.ikora.core.model.Step;
import lu.uni.serval.ikora.core.model.Token;
import lu.uni.serval.ikora.core.model.UserKeyword;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import lu.uni.serval.ikora.core.Helpers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LevenshteinDistanceTest {
    private static UserKeyword keyword1;
    private static UserKeyword keyword2;
    private static UserKeyword keyword3;

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

        Project project = result.getProjects().iterator().next();

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
