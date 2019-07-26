package org.ukwikora.report;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import java.util.ArrayList;
import java.util.List;

public class Suite implements ReportElement {
    @JacksonXmlProperty(localName = "source", isAttribute = true)
    private String source;
    @JacksonXmlProperty(localName = "id", isAttribute = true)
    private String id;
    @JacksonXmlProperty(localName = "name", isAttribute = true)
    private String name;
    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "suite")
    private List<Suite> suites;
    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "test")
    private List<Test> tests;
    @JacksonXmlProperty(localName = "status")
    private Status status;
    @JacksonXmlProperty(localName = "doc")
    private String documentation;

    public Suite(){
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

    public List<Suite> getSuites() {
        return suites;
    }

    public void setSuites(List<Suite> suites) {
        this.suites = suites;
    }

    public void addSuite(Suite suite){
        this.suites.add(suite);
    }

    public List<Test> getTests() {
        return tests;
    }

    public void setTests(List<Test> tests) {
        this.tests = tests;
    }

    public void addTest(Test test) {
        this.tests.add(test);
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getDocumentation() {
        return documentation;
    }

    public void setDocumentation(String documentation) {
        this.documentation = documentation;
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

    public int getNumberTests(Status.Type status) {
        int number = 0;

        for(Suite suite: suites){
            number += suite.getNumberTests(status);
        }

        for(Test test: tests){
            if(status == Status.Type.ANY || test.getStatus().isType(status)){
                number += 1;
            }
        }

        return number;
    }

    @Override
    public void addElement(ReportElement element) throws Exception {
        if(Suite.class.isAssignableFrom(element.getClass())){
            addSuite((Suite)element);
        }
        else if(Test.class.isAssignableFrom(element.getClass())){
            addTest((Test)element);
        }
        else {
            throw new BadElementException(Test.class, element.getClass());
        }
    }
}
