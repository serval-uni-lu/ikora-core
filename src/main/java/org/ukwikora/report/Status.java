package org.ukwikora.report;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlText;

import java.io.IOException;
import java.time.LocalDateTime;

public class Status implements ReportElement {
    public enum Type{
        PASSED, FAILED, NOT_EXECUTED, UNKNOWN, ANY
    }

    @JacksonXmlProperty(localName = "status", isAttribute = true)
    private Type type;
    @JacksonXmlProperty(localName = "endtime", isAttribute = true)
    private LocalDateTime endTime;
    @JacksonXmlProperty(localName = "starttime", isAttribute = true)
    private LocalDateTime startTime;
    @JacksonXmlProperty(localName = "critical", isAttribute = true)
    private boolean critical;
    @JacksonXmlText
    private String text;

    public Status(){
        this.type = Type.NOT_EXECUTED;
    }

    public Type getType() {
        return type;
    }

    public boolean isType(Type type){
        return this.type == type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public void setType(String type){
        if(type.equalsIgnoreCase("PASS") || type.equalsIgnoreCase("PASSED")){
            this.type = Type.PASSED;
        }
        else if(type.equalsIgnoreCase("FAIL") || type.equalsIgnoreCase("FAILED")){
            this.type = Type.FAILED;
        }
        else {
            this.type = Type.UNKNOWN;
        }
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public void setEndTime(String endTime){
        setEndTime(Utils.toLocalDateTime(endTime));
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public void setStartTime(String startTime){
        setStartTime(Utils.toLocalDateTime(startTime));
    }

    public boolean isCritical() {
        return critical;
    }

    public void setCritical(String critical) throws IOException {
        updateCritical(Utils.toBoolean(critical));
    }

    public void updateCritical(boolean critical) {
        this.critical = critical;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
