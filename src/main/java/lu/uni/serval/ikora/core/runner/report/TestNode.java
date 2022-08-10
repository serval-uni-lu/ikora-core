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

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lu.uni.serval.ikora.core.exception.BadElementException;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class TestNode implements ReportElement {
    @JacksonXmlProperty(localName = "id", isAttribute = true)
    private String id;
    @JacksonXmlProperty(localName = "name", isAttribute = true)
    private String name;
    @JacksonXmlElementWrapper(localName = "tags")
    @JacksonXmlProperty(localName = "tag")
    private Set<String> tags;
    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "kw")
    private List<KeywordNode> keywords;
    @JacksonXmlProperty
    private StatusNode status = new StatusNode();
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

    public List<KeywordNode> getKeywords() {
        return keywords;
    }

    public void setKeywords(List<KeywordNode> keywords) {
        this.keywords = keywords;
    }

    public void addKeyword(KeywordNode keyword){
        if(keywords == null){
            keywords = new ArrayList<>();
        }

        keywords.add(keyword);
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

        addKeyword((KeywordNode)element);
    }

    @Override
    public void updateStatus(StatusNode.Type type) {
        status.setType(type);
    }
}
