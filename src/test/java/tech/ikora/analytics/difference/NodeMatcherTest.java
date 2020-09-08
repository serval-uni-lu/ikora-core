package tech.ikora.analytics.difference;

import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.Test;
import tech.ikora.builder.BuildResult;
import tech.ikora.builder.Builder;
import tech.ikora.model.Project;
import tech.ikora.model.UserKeyword;

import java.util.List;

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
}