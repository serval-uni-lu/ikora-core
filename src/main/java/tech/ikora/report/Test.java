package tech.ikora.report;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import tech.ikora.exception.BadElementException;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Test implements ReportElement {
    @JacksonXmlProperty(localName = "id", isAttribute = true)
    private String id;
    @JacksonXmlProperty(localName = "name", isAttribute = true)
    private String name;
    @JacksonXmlElementWrapper(localName = "tags")
    @JacksonXmlProperty(localName = "tag")
    private Set<String> tags;
    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "kw")
    private List<Keyword> keywords;
    @JacksonXmlProperty
    private Status status;
    @JacksonXmlProperty(localName = "doc")
    private String documentation;

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

    public Set<String> getTags() {
        return tags;
    }

    public void setTags(Set<String> tags) {
        this.tags = tags;
    }

    public List<Keyword> getKeywords() {
        return keywords;
    }

    public void setKeywords(List<Keyword> keywords) {
        this.keywords = keywords;
    }

    public void addKeyword(Keyword keyword){
        if(keywords == null){
            keywords = new ArrayList<>();
        }

        keywords.add(keyword);
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

    @Override
    public void addElement(ReportElement element) throws Exception {
        if(!Keyword.class.isAssignableFrom(element.getClass())){
            throw new BadElementException(Keyword.class, element.getClass());
        }

        if(keywords == null){
            keywords = new ArrayList<>();
        }

        addKeyword((Keyword)element);
    }
}
