package org.ukwikora.report;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlText;

import java.text.ParseException;
import java.util.Date;

public class Message {
    @JacksonXmlProperty(localName = "timestamp", isAttribute = true)
    private Date timestamp;
    @JacksonXmlProperty(localName = "level", isAttribute = true)
    private String level;
    @JacksonXmlProperty(localName = "html", isAttribute = true)
    private boolean html;
    @JacksonXmlText
    private String text;

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) throws ParseException{
        setTimestamp(Converter.toDate(timestamp));
    }

    public void setTimestamp(Date timestamp) {
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
