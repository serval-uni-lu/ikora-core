package lu.uni.serval.ikora.core.model;

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


import lu.uni.serval.ikora.core.analytics.visitor.NodeVisitor;
import lu.uni.serval.ikora.core.analytics.visitor.VisitorMemory;
import lu.uni.serval.ikora.core.types.BaseType;
import lu.uni.serval.ikora.core.types.BaseTypeFactory;
import lu.uni.serval.ikora.core.types.BaseTypeList;

import java.util.List;
import java.util.Optional;

public class UserKeyword extends KeywordDefinition {
    private NodeList<Variable> parameters;
    private NodeList<Value> returnVariables;

    private TestProcessing tearDown;

    public UserKeyword(Token name) {
        super(name);

        this.parameters = new NodeList<>(Token.empty());
        this.addAstChild(this.parameters);

        this.returnVariables = new NodeList<>(Token.empty());
        this.addAstChild(this.returnVariables);
    }

    public NodeList<Variable> getParameters() {
        return parameters;
    }

    public Optional<TestProcessing> getTearDown() {
        return Optional.ofNullable(tearDown);
    }

    public Optional<Variable> getParameterByName(Token name) {
        return parameters.stream().filter(a -> a.matches(name)).findFirst();
    }

    public void setParameters(NodeList<Variable> arguments){
        this.parameters = arguments;
        this.addAstChild(this.parameters);
        addTokens(arguments.getTokens());
    }

    public void setReturnVariables(NodeList<Value> returnVariables){
        this.returnVariables = returnVariables;
        this.addAstChild(this.returnVariables);
        addTokens(returnVariables.getTokens());
    }

    public void setTearDown(TestProcessing tearDown) {
        this.tearDown = tearDown;
        this.addAstChild(this.tearDown);
    }

    @Override
    public void addStep(Step step) {
        super.addStep(step);

        if(Assignment.class.isAssignableFrom(step.getClass())){
            addLocalVariables(((Assignment) step).getLeftHandOperand());
        }
    }

    @Override
    public NodeList<Value> getReturnValues() {
        return returnVariables;
    }

    @Override
    public BaseTypeList getArgumentTypes() {
        return new BaseTypeList(parameters.stream()
                .map(BaseTypeFactory::fromVariable)
                .toArray(BaseType[]::new));
    }

    @Override
    public void accept(NodeVisitor visitor, VisitorMemory memory){
        visitor.visit(this, memory);
    }

    @Override
    public List<Dependable> findDefinition(Variable variable) {
        final List<Dependable> definitions = super.findDefinition(variable);

        if(parameters.stream().anyMatch(v -> v.matches(variable.getDefinitionToken()))){
            definitions.add(this);
        }

        return definitions;
    }
}
