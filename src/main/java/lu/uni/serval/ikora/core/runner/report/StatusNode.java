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
