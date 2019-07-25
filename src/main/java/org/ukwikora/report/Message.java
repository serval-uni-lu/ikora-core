package org.ukwikora.report;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlText;

import java.io.IOException;
import java.time.LocalDateTime;

public class Message implements ReportElement{
    @JacksonXmlProperty(localName = "timestamp", isAttribute = true)
    private LocalDateTime timestamp;
    @JacksonXmlProperty(localName = "level", isAttribute = true)
    private String level;
    @JacksonXmlProperty(localName = "html", isAttribute = true)
    private boolean html;
    @JacksonXmlText
    private String text;

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp){
        setTimestamp(Utils.toLocalDateTime(timestamp));
    }

    public void setTimestamp(LocalDateTime timestamp) {
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

    public void setHtml(String html) throws IOException {
        updateHtml(Utils.toBoolean(html));
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
