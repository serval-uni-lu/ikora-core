package lu.uni.serval.ikora.report;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlText;

import java.text.ParseException;
import java.util.Date;

public class Status {
    public enum Type{
        PASSED, FAILED, NOT_EXECUTED, UNKNOWN, ANY
    }

    @JacksonXmlProperty(localName = "status", isAttribute = true)
    private Type type;
    @JacksonXmlProperty(localName = "endtime", isAttribute = true)
    private Date endTime;
    @JacksonXmlProperty(localName = "starttime", isAttribute = true)
    private Date startTime;
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

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public void setEndTime(String endTime) throws ParseException {
        setEndTime(Converter.toDate(endTime));
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public void setStartTime(String startTime) throws ParseException {
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
