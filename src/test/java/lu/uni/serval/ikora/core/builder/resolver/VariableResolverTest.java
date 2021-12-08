package lu.uni.serval.ikora.core.builder.resolver;

import lu.uni.serval.ikora.core.builder.BuildResult;
import lu.uni.serval.ikora.core.builder.Builder;
import lu.uni.serval.ikora.core.model.Project;
import lu.uni.serval.ikora.core.model.VariableAssignment;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class VariableResolverTest {
    @Test
    void testVariableAssignmentLinks(){
        final String code =
                "*** Settings ***\n" +
                        "Library    Selenium2Library\n" +
                        "*** Test Cases ***\n" +
                        "Valid Login\n" +
                        "    Open Browser To Login Page\n" +
                        "\n" +
                        "*** Keywords ***\n" +
                        "Open Browser To Login Page\n" +
                        "    Open Browser    ${SERVER}    chrome\n" +
                        "    Set Selenium Speed    0\n" +
                        "    Maximize Browser Window\n" +
                        "\n" +
                        "*** Variables ***\n" +
                        "${SERVER}         localhost:7272\n" +
                        "${LOGIN URL}      http://${SERVER}/\n";

        final BuildResult build = Builder.build(code, true);
        final Project project = build.getProjects().iterator().next();
        final Set<VariableAssignment> found = project.findVariable(null, "${SERVER}");

        assertEquals(1, found.size());
        final VariableAssignment variableAssignment = found.iterator().next();
        assertEquals(2, variableAssignment.getDependencies().size());
    }
}