package org.ukwikora.report;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import java.io.IOException;
import java.util.List;

public class Keyword implements ReportElement {
    public enum Type{
        Setup, Execution, TearDown
    }

    @JacksonXmlProperty(localName = "type", isAttribute = true)
    private Type type;
    @JacksonXmlProperty(localName = "name", isAttribute = true)
    private String name;
    @JacksonXmlProperty(localName = "library", isAttribute = true)
    private String library;
    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "kw")
    private List<Keyword> keywords;
    @JacksonXmlElementWrapper(localName = "arguments")
    @JacksonXmlProperty(localName = "arg")
    private List<String> arguments;
    @JacksonXmlElementWrapper(localName = "assign")
    @JacksonXmlProperty(localName = "var")
    private List<String> assigments;
    @JacksonXmlElementWrapper(localName = "tags")
    @JacksonXmlProperty(localName = "tag")
    private List<String> tags;
    @JacksonXmlProperty(localName = "msg")
    private Message message;
    @JacksonXmlProperty(localName = "status")
    private Status status;
    @JacksonXmlProperty(localName = "doc")
    private String documentation;

    public Type getType() {
        return type;
    }

    public void setType(String type) throws IOException {
        if(type.equalsIgnoreCase("Setup")){
            setType(Type.Setup);
        }
        else if(type.equalsIgnoreCase("Execution")){
            setType(Type.Execution);
        }
        else if(type.equalsIgnoreCase("TearDown")){
            setType(Type.TearDown);
        }
        else{
            throw new IOException(String.format("Could not convert %s to keyword type. Excepted one of [setup, execution,teardown]"));
        }
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLibrary() {
        return library;
    }

    public void setLibrary(String library) {
        this.library = library;
    }

    public List<Keyword> getKeywords() {
        return keywords;
    }

    public void setKeywords(List<Keyword> keywords) {
        this.keywords = keywords;
    }

    public List<String> getArguments() {
        return arguments;
    }

    public void setArguments(List<String> arguments) {
        this.arguments = arguments;
    }

    public List<String> getAssigments() {
        return assigments;
    }

    public void setAssigments(List<String> assigments) {
        this.assigments = assigments;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
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
}
