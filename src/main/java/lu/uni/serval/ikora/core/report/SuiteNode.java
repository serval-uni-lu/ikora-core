package lu.uni.serval.ikora.core.report;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lu.uni.serval.ikora.core.exception.BadElementException;

import java.util.ArrayList;
import java.util.List;

public class SuiteNode implements ReportElement {
    @JacksonXmlProperty(localName = "source", isAttribute = true)
    private String source;
    @JacksonXmlProperty(localName = "id", isAttribute = true)
    private String id;
    @JacksonXmlProperty(localName = "name", isAttribute = true)
    private String name;
    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "suite")
    private List<SuiteNode> suites;
    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "test")
    private List<TestNode> tests;
    @JacksonXmlProperty(localName = "status")
    private StatusNode status;
    @JacksonXmlProperty(localName = "doc")
    private String documentation;

    public SuiteNode(){
        this.suites = new ArrayList<>();
        this.tests = new ArrayList<>();
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<SuiteNode> getSuites() {
        return suites;
    }

    public void setSuites(List<SuiteNode> suites) {
        this.suites = suites;
    }

    public void addSuite(SuiteNode suite){
        this.suites.add(suite);
    }

    public List<TestNode> getTests() {
        return tests;
    }

    public void setTests(List<TestNode> tests) {
        this.tests = tests;
    }

    public void addTest(TestNode test) {
        this.tests.add(test);
    }

    public StatusNode getStatus() {
        return status;
    }

    public void setStatus(StatusNode status) {
        this.status = status;
    }

    public String getDocumentation() {
        return documentation;
    }

    public void setDocumentation(String documentation) {
        this.documentation = documentation;
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

    public int getNumberTests(StatusNode.Type status) {
        int number = 0;

        for(SuiteNode suite: suites){
            number += suite.getNumberTests(status);
        }

        for(TestNode test: tests){
            if(status == StatusNode.Type.ANY || test.getStatus().isType(status)){
                number += 1;
            }
        }

        return number;
    }

    @Override
    public void addElement(ReportElement element) throws BadElementException {
        if(SuiteNode.class.isAssignableFrom(element.getClass())){
            addSuite((SuiteNode)element);
        }
        else if(TestNode.class.isAssignableFrom(element.getClass())){
            addTest((TestNode)element);
        }
        else {
            throw new BadElementException(TestNode.class, element.getClass());
        }
    }
}
