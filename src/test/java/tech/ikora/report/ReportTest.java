package tech.ikora.report;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.junit.jupiter.api.Test;
import tech.ikora.utils.FileUtils;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

import static org.junit.jupiter.api.Assertions.*;

public class ReportTest {
    @Test
    void testParsingOfReportWithOneFailingTest() throws IOException, URISyntaxException {
        File xmlFile = FileUtils.getResourceFile("robot/reports/report-1.xml");
        XmlMapper xmlMapper = new XmlMapper();

        try {
            Report report = xmlMapper.readValue(xmlFile, Report.class);
            assertEquals(1, report.getSuites().size());
            assertEquals(1, report.getNumberTests());
            assertEquals(0, report.getNumberTests(Status.Type.PASSED));
            assertEquals(1, report.getNumberTests(Status.Type.FAILED));

            final Suite loginSuite = report.getSuites().get(0);
            assertEquals("Login Tests", loginSuite.getName());
            assertEquals(1, loginSuite.getNumberTests(Status.Type.ANY));
            assertEquals(1, loginSuite.getSuites().size());

            final Suite gherkinSuite = loginSuite.getSuites().get(0);
            assertEquals("Gherkin Login", gherkinSuite.getName());
            assertEquals(1, gherkinSuite.getNumberTests(Status.Type.ANY));
            assertEquals(0, gherkinSuite.getSuites().size());

            final tech.ikora.report.Test test = gherkinSuite.getTests().get(0);
            assertTrue(test.getStatus().isType(Status.Type.FAILED));
            assertEquals("s1-s1-t1", test.getId());
            assertEquals("Gherkin Valid Login", test.getName());
            assertEquals(4, test.getKeywords().size());
            assertEquals(2, test.getTags().size());

            final Keyword keyword1 = test.getKeywords().get(0);
            assertEquals("Given browser is opened to login page", keyword1.getName());
            assertEquals(Keyword.Type.EXECUTION, keyword1.getType());

            final Keyword keyword4 = test.getKeywords().get(3);
            assertEquals("Close Browser", keyword4.getName());
            assertEquals(Keyword.Type.TEAR_DOWN, keyword4.getType());

        } catch (IOException e) {
            fail(String.format("failed to parse report-1.xml: %s", e.getMessage()));
        }
    }
}
