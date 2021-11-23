package lu.uni.serval.ikora.core.report;

/*-
 * #%L
 * Ikora Core
 * %%
 * Copyright (C) 2019 - 2021 University of Luxembourg
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License")
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lu.uni.serval.ikora.core.exception.BadElementException;

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
