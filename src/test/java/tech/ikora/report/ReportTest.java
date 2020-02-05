package tech.ikora.report;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.junit.jupiter.api.Test;
import tech.ikora.Helpers;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class ReportTest {
    @Test
    void checkTypicalReportParsing(){
        File xmlFile = Helpers.getResourceFile("robot/reports/report-1.xml");
        XmlMapper xmlMapper = new XmlMapper();

        try {
            Report report = xmlMapper.readValue(xmlFile, Report.class);
        } catch (IOException e) {
            fail(String.format("failed to parse report-1.xml: %s", e.getMessage()));
        }
    }
}