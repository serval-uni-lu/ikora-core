package lu.uni.serval.ikora.core.report;

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
