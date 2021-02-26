package lu.uni.serval.ikora.core.report;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlText;

import java.text.ParseException;
import java.time.Instant;

public class StatusNode {
    public enum Type{
        PASSED, FAILED, NOT_EXECUTED, UNKNOWN, ANY
    }

    @JacksonXmlProperty(localName = "status", isAttribute = true)
    private Type type;
    @JacksonXmlProperty(localName = "endtime", isAttribute = true)
    private Instant endTime;
    @JacksonXmlProperty(localName = "starttime", isAttribute = true)
    private Instant startTime;
    @JacksonXmlProperty(localName = "critical", isAttribute = true)
    private boolean critical;
    @JacksonXmlText
    private String text;

    public StatusNode(){
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

    public Instant getEndTime() {
        return endTime;
    }

    public void setEndTime(Instant endTime) {
        this.endTime = endTime;
    }

    public void setEndTime(String endTime) {
        setEndTime(Converter.toDate(endTime));
    }

    public Instant getStartTime() {
        return startTime;
    }

    public void setStartTime(Instant startTime) {
        this.startTime = startTime;
    }

    public void setStartTime(String startTime) {
        setStartTime(Converter.toDate(startTime));
    }

    public boolean isCritical() {
        return critical;
    }

    public void setCritical(String critical) throws ParseException {
        updateCritical(Converter.toBoolean(critical));
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
