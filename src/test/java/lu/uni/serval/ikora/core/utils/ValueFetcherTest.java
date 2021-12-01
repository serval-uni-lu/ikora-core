package lu.uni.serval.ikora.core.utils;

/*-
 * #%L
 * Ikora Core
 * %%
 * Copyright (C) 2019 - 2021 University of Luxembourg
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License")
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import lu.uni.serval.ikora.core.builder.BuildResult;
import lu.uni.serval.ikora.core.builder.Builder;
import lu.uni.serval.ikora.core.model.*;
import lu.uni.serval.ikora.core.types.ListType;
import lu.uni.serval.ikora.core.types.LocatorType;
import lu.uni.serval.ikora.core.types.StringType;
import lu.uni.serval.ikora.core.types.TimeoutType;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class ValueFetcherTest {
    @Test
    void testGetArgumentTypeForUserKeywordWithLiterals(){
        final String code =
                "*** Test Cases ***\n" +
                "\n" +
                "Test connexion\n" +
                "    Connexion to the service    username    password\n" +
                "\n" +
                "*** Keywords ***\n" +
                "\n" +
                "Connexion to the service\n" +
                "    [Arguments]  ${user}    ${pwd}\n" +
                "    Log    Connecting to service with ${user} and ${pwd}";

        final BuildResult result = Builder.build(code, true);
        final Project project = result.getProjects().iterator().next();
        final TestCase testCase = project.findTestCase("", "Test connexion").iterator().next();
        final NodeList<Argument> argumentList = testCase.getStep(0).getArgumentList();

        assertEquals(StringType.class, argumentList.get(0).getType().getClass());
        assertEquals(StringType.class, argumentList.get(1).getType().getClass());
    }

    @Test
    void testGetArgumentTypeForUserKeywordWithList(){
        final String code =
                "*** Test Cases ***\n" +
                        "\n" +
                        "Test connexion\n" +
                        "    @{credentials}=    Create List    username    password\n" +
                        "    Connexion to the service    @{credentials}\n" +
                        "\n" +
                        "*** Keywords ***\n" +
                        "\n" +
                        "Connexion to the service\n" +
                        "    [Arguments]  ${user}    ${pwd}\n" +
                        "    Log    Connecting to service with ${user} and ${pwd}";

        final BuildResult result = Builder.build(code, true);
        final Project project = result.getProjects().iterator().next();
        final TestCase testCase = project.findTestCase("", "Test connexion").iterator().next();
        final NodeList<Argument> argumentList = testCase.getStep(0).getArgumentList();

        assertEquals(ListType.class, argumentList.get(0).getType().getClass());
    }

    @Test
    void testGetArgumentTypeForLibraryKeywordWithLocator(){
        final String code =
                "*** Settings ***\n" +
                "Library    Selenium2Library\n" +
                "*** Test Cases ***\n" +
                "\n" +
                "Test click button\n" +
                "    Click Button    button_id\n";

        final BuildResult result = Builder.build(code, true);
        final Project project = result.getProjects().iterator().next();
        final TestCase testCase = project.findTestCase("", "Test click button").iterator().next();
        final NodeList<Argument> argumentList = testCase.getStep(0).getArgumentList();

        assertEquals(LocatorType.class, argumentList.get(0).getType().getClass());
    }

    @Test
    void testGetArgumentTypeForLibraryKeywordWithTimeout(){
        final String code =
                "*** Test Cases ***\n" +
                        "\n" +
                        "Test sleep\n" +
                        "    Sleep    10 seconds\n";

        final BuildResult result = Builder.build(code, true);
        final Project project = result.getProjects().iterator().next();
        final TestCase testCase = project.findTestCase("", "Test sleep").iterator().next();
        final NodeList<Argument> argumentList = testCase.getStep(0).getArgumentList();

        assertEquals(TimeoutType.class, argumentList.get(0).getType().getClass());
    }

    @Test
    void testArgumentValuesFromAssignment(){
        final String code =
                "*** Keywords ***\n" +
                "Some keyword\n" +
                "    Log    ${value}\n" +
                "*** Variables ***\n" +
                "${value}    value-${env}\n" +
                "${env}    test\n";

        final BuildResult result = Builder.build(code, true);
        final Project project = result.getProjects().iterator().next();

        UserKeyword keyword = project.findUserKeyword(Token.fromString("Some keyword")).iterator().next();

        final Set<String> values = ValueFetcher.getValues(keyword.getStep(0).getArgumentList().get(0));
        assertEquals(1, values.size());
        assertEquals("value-test", values.iterator().next());
    }


    @Test
    void testArgumentValuesFromKeywordParameter(){
        final String code =
                "*** Keywords ***\n" +
                "Caller\n" +
                "    Some keyword    ${value}\n" +
                "Some keyword\n" +
                "    [Arguments]    ${arg}\n" +
                "    Log    ${arg}\n" +
                "*** Variables ***\n" +
                "${value}    value-${env}\n" +
                "${env}    test\n";

        final BuildResult result = Builder.build(code, true);
        final Project project = result.getProjects().iterator().next();

        UserKeyword keyword = project.findUserKeyword(Token.fromString("Some keyword")).iterator().next();

        final Set<String> values = ValueFetcher.getValues(keyword.getStep(0).getArgumentList().get(0));
        assertEquals(1, values.size());
        assertEquals("value-test", values.iterator().next());
    }

    @Test
    void testArgumentValuesFromKeywordParameterRecursive(){
        final String code =
                "*** Keywords ***\n" +
                "Some keyword\n" +
                "    [Arguments]    ${arg}\n" +
                "    Some keyword    ${arg}\n";

        final BuildResult result = Builder.build(code, true);
        final Project project = result.getProjects().iterator().next();

        UserKeyword keyword = project.findUserKeyword(Token.fromString("Some keyword")).iterator().next();

        final Set<String> values = ValueFetcher.getValues(keyword.getStep(0).getArgumentList().get(0));
        assertEquals(0, values.size());
    }

    @Test
    void testMultipleArgumentValues(){
        final String code =
            "*** Settings ***\n" +
            "Library           SeleniumLibrary\n" +
            "\n" +
            "*** Test Cases ***\n" +
            "Run the test\n" +
            "    Keyword calling other keyword with parameters\n" +
            "\n" +
            "*** Keywords ***\n" +
            "Keyword calling other keyword with parameters\n" +
            "    Follow the link  ${simple}\n" +
            "    Follow the link  ${complex}\n" +
            "\n" +
            "Follow the link\n" +
            "    [Arguments]    ${locator}\n" +
            "    Click Link    ${locator}\n" +
            "\n" +
            "*** Variables ***\n" +
            "${simple}  link_to_click\n" +
            "${complex}  css:.covid-form > div";

        final BuildResult result = Builder.build(code, true);
        final Project project = result.getProjects().iterator().next();

        final UserKeyword followLink = project.findUserKeyword(FileUtils.IN_MEMORY, "Follow the link").iterator().next();
        assertNotNull(followLink);

        final Set<String> values = ValueFetcher.getValues(followLink.getStep(0).getArgumentList().get(0));
        assertEquals(2, values.size());

        assertTrue(values.contains("link_to_click"));
        assertTrue(values.contains("css:.covid-form > div"));
    }

    @Test
    void testRecursiveCallWithRunIf(){
        final String code =
            "*** Keywords ***\n" +
            "Run something conditionally\n" +
            "\t[Arguments]\t${parameter}\n"+
            "\tRun Keyword If\t'''${should_do_it}'''=='''True'''\tSomething\t${parameter}\n"+
            "Something\n" +
            "\t[Arguments]\t${parameter}\n"+
            "\tRun something conditionally\t${parameter}\n";

        final BuildResult result = Builder.build(code, true);
        final Project project = result.getProjects().iterator().next();

        final UserKeyword something = project.findUserKeyword(FileUtils.IN_MEMORY, "Something").iterator().next();
        assertNotNull(something);

        final Set<String> values = ValueFetcher.getValues(something.getStep(0).getArgumentList().get(0));
        assertEquals(0, values.size());
    }
}
