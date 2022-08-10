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
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import static org.junit.jupiter.api.Assertions.*;

class StatusNodeTest {
    @Test
    void checkStatusMessageParsedFromXmlString() throws IOException {
        String xml = "<status status=\"PASS\" endtime=\"20170220 14:19:07.693\" starttime=\"20170220 14:18:55.937\"></status>";
        XmlMapper xmlMapper = new XmlMapper();

        StatusNode status = xmlMapper.readValue(xml, StatusNode.class);

        final Instant startTime = LocalDateTime.of(2017, 2, 20, 14, 18, 55, 937000000)
                .atZone(ZoneOffset.UTC).toInstant();

        final Instant endTime = LocalDateTime.of(2017, 2, 20, 14, 19,7, 693000000)
                .atZone(ZoneOffset.UTC).toInstant();

        assertEquals(StatusNode.Type.PASSED, status.getType());
        assertEquals(status.getStartTime(), startTime);
        assertEquals(status.getEndTime(), endTime);
    }
}