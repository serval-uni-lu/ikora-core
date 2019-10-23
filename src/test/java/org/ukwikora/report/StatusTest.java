package org.ukwikora.report;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class StatusTest {
    @Test
    void checkStatusMessageParsedFromXmlString() throws IOException {
        String xml = "<status status=\"PASS\" endtime=\"20170220 14:19:07.693\" starttime=\"20170220 14:18:55.937\"></status>";
        XmlMapper xmlMapper = new XmlMapper();

        Status status = xmlMapper.readValue(xml, Status.class);

        final Instant startTime = LocalDateTime.of(2017, 2, 20, 14, 18, 55, 937000000)
                .atZone(ZoneId.systemDefault()).toInstant();

        final Instant endTime = LocalDateTime.of(2017, 2, 20, 14, 19,7, 693000000)
                .atZone(ZoneId.systemDefault()).toInstant();

        assertEquals(status.getType(), Status.Type.PASSED);
        assertEquals(status.getStartTime(), Date.from(startTime));
        assertEquals(status.getEndTime(), Date.from(endTime));
    }
}
