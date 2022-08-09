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
package lu.uni.serval.ikora.core.analytics.difference;

import lu.uni.serval.ikora.core.builder.BuildResult;
import lu.uni.serval.ikora.core.builder.Builder;
import lu.uni.serval.ikora.core.model.Project;
import lu.uni.serval.ikora.core.model.SourceNode;
import lu.uni.serval.ikora.core.model.Token;
import lu.uni.serval.ikora.core.model.UserKeyword;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class NodeMatcherTest {
    @Test
    void testRemoveKeyword(){
        final String code1 =
                "*** Test Cases ***\n" +
                        "Valid Login\n" +
                        "    Open Browser To Login Page\n" +
                        "\n" +
                        "*** Keywords ***\n" +
                        "Open Browser To Login Page\n" +
                        "    Open Browser    http://localhost/    chrome\n" +
                        "    Set Selenium Speed    ${DELAY}\n" +
                        "    Maximize Browser Window\n" +
                        "    Title Should Be    Login Page\n" +
                        "Open Again Browser To Login Page\n" +
                        "    Open Browser    http://localhost/    chrome\n" +
                        "    Maximize Browser Window\n" +
                        "    Title Should Be    Login Page\n" +
                        "\n" +
                        "*** Variables ***\n" +
                        "${DELAY}      0\n";

        final BuildResult build1 = Builder.build(code1, true);
        final Project project1 = build1.getProjects().iterator().next();

        final String code2 =
                "*** Test Cases ***\n" +
                        "Valid Login\n" +
                        "    Open Browser To Login Page\n" +
                        "\n" +
                        "*** Keywords ***\n" +
                        "Open Browser To Login Page\n" +
                        "    Open Browser    http://localhost/    chrome\n" +
                        "    Set Selenium Speed    ${DELAY}\n" +
                        "    Maximize Browser Window\n" +
                        "    Title Should Be    Login Page\n" +
                        "\n" +
                        "*** Variables ***\n" +
                        "${DELAY}      0\n";

        final BuildResult build2 = Builder.build(code2, true);
        final Project project2 = build2.getProjects().iterator().next();

        final List<Pair<UserKeyword, UserKeyword>> pairs = NodeMatcher.getPairs(project1.getUserKeywords(), project2.getUserKeywords(), false);

        assertEquals(2, pairs.size());
    }

    @Test
    void testVersionsPairRemoveKeyword(){
        final String code1 =
                "*** Test Cases ***\n" +
                        "Valid Login\n" +
                        "    Open Browser To Login Page\n" +
                        "\n" +
                        "*** Keywords ***\n" +
                        "Open Browser To Login Page\n" +
                        "    Open Browser    http://localhost/    chrome\n" +
                        "    Set Selenium Speed    ${DELAY}\n" +
                        "    Maximize Browser Window\n" +
                        "    Title Should Be    Login Page\n" +
                        "\n" +
                        "*** Variables ***\n" +
                        "${DELAY}      0\n";

        final BuildResult build1 = Builder.build(code1, true);

        final String code2 =
                "*** Test Cases ***\n" +
                        "Valid Login\n" +
                        "    Open Browser To Login Page\n" +
                        "\n" +
                        "*** Keywords ***\n" +
                        "Open Browser To Login Page\n" +
                        "    Open Browser    http://localhost/    firefox\n" +
                        "    Set Selenium Speed    ${DELAY}\n" +
                        "    Maximize Browser Window\n" +
                        "    Title Should Be    Login Page\n" +
                        "Open Again Browser To Login Page\n" +
                        "    Open Browser    http://localhost/    chrome\n" +
                        "    Maximize Browser Window\n" +
                        "    Title Should Be    Login Page\n" +
                        "\n" +
                        "*** Variables ***\n" +
                        "${DELAY}      0\n";

        final BuildResult build2 = Builder.build(code2, true);

        final VersionPairs versionPairs = NodeMatcher.computeVersionsPairs(build1.getProjects(), build2.getProjects(), false);

        final UserKeyword k1_1 = build1.getProjects().iterator().next().findUserKeyword(Token.fromString("Open Browser To Login Page")).iterator().next();
        final UserKeyword k2_1 = build2.getProjects().iterator().next().findUserKeyword(Token.fromString("Open Browser To Login Page")).iterator().next();
        assertEquals(k1_1, versionPairs.findPrevious(k2_1).get());

        final UserKeyword k2_2 = build2.getProjects().iterator().next().findUserKeyword(Token.fromString("Open Again Browser To Login Page")).iterator().next();
        assertEquals(Optional.empty(), versionPairs.findPrevious(k2_2));

        assertEquals("chrome", versionPairs.findPrevious(k2_1.getStep(0).getArgumentList().get(1)).get().getName());
    }
}
