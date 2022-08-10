/*
 *
 *     Copyright © 2022 University of Luxembourg
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
package lu.uni.serval.ikora.core.runner.report;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import lu.uni.serval.ikora.core.utils.FileUtils;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

import static org.junit.jupiter.api.Assertions.*;

class ReportTest {
    @Test
    void testParsingOfReportWithOneFailingTest() throws IOException, URISyntaxException {
        File xmlFile = FileUtils.getResourceFile("reports/report-1.xml");
        XmlMapper xmlMapper = new XmlMapper();

        try {
            Report report = xmlMapper.readValue(xmlFile, Report.class);
            assertEquals(1, report.getSuites().size());
            assertEquals(1, report.getNumberTests());
            assertEquals(0, report.getNumberTests(StatusNode.Type.PASSED));
            assertEquals(1, report.getNumberTests(StatusNode.Type.FAILED));

            final SuiteNode loginSuite = report.getSuites().get(0);
            assertEquals("Login Tests", loginSuite.getName());
            assertEquals(1, loginSuite.getNumberTests(StatusNode.Type.ANY));
            assertEquals(1, loginSuite.getSuites().size());

            final SuiteNode gherkinSuite = loginSuite.getSuites().get(0);
            assertEquals("Gherkin Login", gherkinSuite.getName());
            assertEquals(1, gherkinSuite.getNumberTests(StatusNode.Type.ANY));
            assertEquals(0, gherkinSuite.getSuites().size());

            final TestNode test = gherkinSuite.getTests().get(0);
            assertTrue(test.getStatus().isType(StatusNode.Type.FAILED));
            assertEquals("s1-s1-t1", test.getId());
            assertEquals("Gherkin Valid Login", test.getName());
            assertEquals(4, test.getKeywords().size());
            assertEquals(2, test.getTags().size());

            final KeywordNode keyword1 = test.getKeywords().get(0);
            assertEquals("Given browser is opened to login page", keyword1.getName());
            assertEquals(KeywordNode.Type.EXECUTION, keyword1.getType());

            final KeywordNode keyword4 = test.getKeywords().get(3);
            assertEquals("Close Browser", keyword4.getName());
            assertEquals(KeywordNode.Type.TEAR_DOWN, keyword4.getType());

        } catch (IOException e) {
            fail(String.format("failed to parse report-1.xml: %s", e.getMessage()));
        }
    }
}