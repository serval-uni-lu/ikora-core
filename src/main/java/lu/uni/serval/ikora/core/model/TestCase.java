/*
 *
 *     Copyright © 2019 - 2022 University of Luxembourg
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
package lu.uni.serval.ikora.core.model;

import lu.uni.serval.ikora.core.analytics.visitor.NodeVisitor;
import lu.uni.serval.ikora.core.analytics.visitor.VisitorMemory;
import lu.uni.serval.ikora.core.types.BaseTypeList;

import java.util.Optional;

public class TestCase extends KeywordDefinition {
    private TestProcessing setup = null;
    private TestProcessing tearDown = null;
    private TestProcessing template = null;

    public TestCase(Token name) {
        super(name);
    }

    public void setSetup(TestProcessing setup){
        this.setup = setup;
        this.addAstChild(setup);
    }

    public void setTearDown(TestProcessing tearDown){
        this.tearDown = tearDown;
        this.addAstChild(tearDown);
    }

    public void setTemplate(TestProcessing template){
        this.template = template;
        this.addAstChild(template);
    }

    public Optional<TestProcessing> getSetup(){
        return Optional.ofNullable(setup);
    }

    public Optional<TestProcessing> getTearDown(){
        return Optional.ofNullable(tearDown);
    }

    public Optional<TestProcessing> getTemplate() {
        return Optional.ofNullable(template);
    }

    public NodeList<Step> getExecutedSteps(){
        return getTemplate()
                .map(TestProcessing::getCall)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(Step::getSteps)
                .orElseGet(this::getSteps);
    }

    public boolean hasTemplate(){
        return template != null;
    }

    @Override
    public boolean isDeadCode(){
        return false;
    }

    @Override
    public void accept(NodeVisitor visitor, VisitorMemory memory){
        visitor.visit(this, memory);
    }

    @Override
    public NodeList<Value> getReturnValues() {
        return new NodeList<>();
    }

    @Override
    public BaseTypeList getArgumentTypes() {
        return new BaseTypeList();
    }
}
