package lu.uni.serval.ikora.core.report;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lu.uni.serval.ikora.core.exception.BadElementException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class KeywordNode implements ReportElement {
    public enum Type{
        SETUP, EXECUTION, TEAR_DOWN
    }

    @JacksonXmlProperty(localName = "type", isAttribute = true)
    private Type type = Type.EXECUTION;
    @JacksonXmlProperty(localName = "name", isAttribute = true)
    private String name;
    @JacksonXmlProperty(localName = "library", isAttribute = true)
    private String library;
    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "kw")
    private List<KeywordNode> keywords;
    @JacksonXmlElementWrapper(localName = "arguments")
    @JacksonXmlProperty(localName = "arg")
    private List<String> arguments;
    @JacksonXmlElementWrapper(localName = "assign")
    @JacksonXmlProperty(localName = "var")
    private List<String> assignments;
    @JacksonXmlElementWrapper(localName = "tags")
    @JacksonXmlProperty(localName = "tag")
    private List<String> tags;
    @JacksonXmlProperty(localName = "msg")
    private MessageNode message;
    @JacksonXmlProperty(localName = "status")
    private StatusNode status;
    @JacksonXmlProperty(localName = "doc")
    private String documentation;

    public Type getType() {
        return type;
    }

    public void setType(String type) throws IOException {
        if(type.equalsIgnoreCase("Setup")){
            setType(Type.SETUP);
        }
        else if(type.equalsIgnoreCase("Execution")){
            setType(Type.EXECUTION);
        }
        else if(type.equalsIgnoreCase("TearDown")){
            setType(Type.TEAR_DOWN);
        }
        else{
            throw new IOException(String.format("Could not convert %s to keyword type. Excepted one of [setup, execution,teardown]", type));
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

    public List<KeywordNode> getKeywords() {
        return keywords;
    }

    public void setKeywords(List<KeywordNode> keywords) {
        this.keywords = keywords;
    }

    public List<String> getArguments() {
        return arguments;
    }

    public void setArguments(List<String> arguments) {
        this.arguments = arguments;
    }

    public List<String> getAssignments() {
        return assignments;
    }

    public void setAssignments(List<String> assignments) {
        this.assignments = assignments;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public MessageNode getMessage() {
        return message;
    }

    public void setMessage(MessageNode message) {
        this.message = message;
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

    @Override
    public void addElement(ReportElement element) throws BadElementException {
        if(!KeywordNode.class.isAssignableFrom(element.getClass())){
            throw new BadElementException(KeywordNode.class, element.getClass());
        }

        if(keywords == null){
            keywords = new ArrayList<>();
        }

        keywords.add((KeywordNode)element);
    }
}
