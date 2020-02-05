package tech.ikora.report;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@JacksonXmlRootElement(localName = "robot")
@JsonIgnoreProperties(value ={"statistics", "errors"})
public class Report implements ReportElement {
    @JacksonXmlProperty(localName = "generated", isAttribute = true)
    private Date generated;
    @JacksonXmlProperty(localName = "generator", isAttribute = true)
    private String generator;
    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "suite")
    private List<Suite> suites;

    public Report(){
        suites = new ArrayList<>();
    }

    public Date getGenerated() {
        return generated;
    }

    public void setGenerated(String generated) throws ParseException {
        setGenerated(Converter.toDate(generated));
    }

    public void setGenerated(Date generated) {
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

    public int getNumberTests(){
        return getNumberTests(Status.Type.ANY);
    }

    public int getNumberPassingTests(){
        return getNumberTests(Status.Type.PASSED);
    }

    public int getNumberFailingTests(){
        return getNumberTests(Status.Type.FAILED);
    }

    public int getNumberTests(Status.Type status){
        int number = 0;

        for(Suite suite: suites){
            number += suite.getNumberTests(status);
        }

        return number;
    }

    @Override
    public void addElement(ReportElement element) throws Exception {
        if(!Suite.class.isAssignableFrom(element.getClass())){
            throw new BadElementException(Suite.class, element.getClass());
        }

        if(suites == null){
            suites = new ArrayList<>();
        }

        suites.add((Suite)element);
    }
}
