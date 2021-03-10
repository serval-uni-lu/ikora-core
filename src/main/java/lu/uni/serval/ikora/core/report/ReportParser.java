package lu.uni.serval.ikora.core.report;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import java.io.File;
import java.io.IOException;

public class ReportParser {
    private ReportParser() {}

    public static Report parse(File xmlFile) throws IOException {
        if(!xmlFile.exists()){
            throw new IOException(String.format("Cannot parse report file %s, because it cannot be found",
                    xmlFile.getAbsolutePath()));
        }

        XmlMapper xmlMapper = new XmlMapper();
        return xmlMapper.readValue(xmlFile, Report.class);
    }
}
