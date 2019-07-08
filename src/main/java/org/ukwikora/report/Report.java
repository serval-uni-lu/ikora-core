package org.ukwikora.report;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@JacksonXmlRootElement(localName = "robot")
public class Report {
    @JacksonXmlProperty(localName = "generated", isAttribute = true)
    private LocalDateTime generated;
    @JacksonXmlProperty(localName = "generator", isAttribute = true)
    private String generator;
    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "suite")
    private List<Suite> suites;

    public Report(){
        suites = new ArrayList<>();
    }

    public LocalDateTime getGenerated() {
        return generated;
    }

    public void setGenerated(String generated) {
        setGenerated(Utils.toLocalDateTime(generated));
    }

    public void setGenerated(LocalDateTime generated) {
        this.generated = generated;
    }

    public String getGenerator() {
        return generator;
    }

    public void setGenerator(String generator) {
        this.generator = generator;
    }

    public List<Suite> getSuites() {
        return suites;
    }

    public void setSuites(List<Suite> suites){
        this.suites = suites;
    }

    public void addSuite(Suite suite) {
        suites.add(suite);
    }
}
