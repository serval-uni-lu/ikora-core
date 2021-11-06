package lu.uni.serval.ikora.core.report;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lu.uni.serval.ikora.core.exception.BadElementException;

import java.text.ParseException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@JacksonXmlRootElement(localName = "robot")
@JsonIgnoreProperties(value ={"statistics", "errors"})
public class Report implements ReportElement {
    @JacksonXmlProperty(localName = "generated", isAttribute = true)
    private Instant generated;
    @JacksonXmlProperty(localName = "generator", isAttribute = true)
    private String generator;
    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "suite")
    private List<SuiteNode> suites;

    public Report(){
        suites = new ArrayList<>();
    }

    public Instant getGenerated() {
        return generated;
    }

    public void setGenerated(String generated) {
        setGenerated(Converter.toDate(generated));
    }

    public void setGenerated(Instant generated) {
        this.generated = generated;
    }

    public String getGenerator() {
        return generator;
    }

    public void setGenerator(String generator) {
        this.generator = generator;
    }

    public List<SuiteNode> getSuites() {
        return suites;
    }

    public void setSuites(List<SuiteNode> suites){
        this.suites = suites;
    }

    public void addSuite(SuiteNode suite) {
        suites.add(suite);
    }

    public int getNumberTests(){
        return getNumberTests(StatusNode.Type.ANY);
    }

    public int getNumberPassingTests(){
        return getNumberTests(StatusNode.Type.PASSED);
    }

    public int getNumberFailingTests(){
        return getNumberTests(StatusNode.Type.FAILED);
    }

    public int getNumberTests(StatusNode.Type status){
        int number = 0;

        for(SuiteNode suite: suites){
            number += suite.getNumberTests(status);
        }

        return number;
    }

    @Override
    public void addElement(ReportElement element) throws BadElementException {
        if(!SuiteNode.class.isAssignableFrom(element.getClass())){
            throw new BadElementException(SuiteNode.class, element.getClass());
        }

        if(suites == null){
            suites = new ArrayList<>();
        }

        suites.add((SuiteNode)element);
    }
}
