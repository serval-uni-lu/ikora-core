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

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlText;

import java.text.ParseException;
import java.time.Instant;

public class MessageNode {
    @JacksonXmlProperty(localName = "timestamp", isAttribute = true)
    private Instant timestamp;
    @JacksonXmlProperty(localName = "level", isAttribute = true)
    private String level;
    @JacksonXmlProperty(localName = "html", isAttribute = true)
    private boolean html;
    @JacksonXmlText
    private String text;

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        setTimestamp(Converter.toDate(timestamp));
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public boolean isHtml() {
        return html;
    }

    public void setHtml(String html) throws ParseException {
        updateHtml(Converter.toBoolean(html));
    }

    public void updateHtml(boolean html) {
        this.html = html;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
