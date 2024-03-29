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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserKeyword extends KeywordDefinition {
    private NodeList<Argument> arguments;
    private NodeList<Value> returnVariables;

    private TestProcessing tearDown;

    public UserKeyword(Token name) {
        super(name);

        this.arguments = new NodeList<>(Token.empty());
        this.addAstChild(this.arguments);

        this.returnVariables = new NodeList<>(Token.empty());
        this.addAstChild(this.returnVariables);
    }

    public NodeList<Argument> getArguments() {
        return arguments;
    }

    public Optional<TestProcessing> getTearDown() {
        return Optional.ofNullable(tearDown);
    }

    public Optional<Argument> getParameterByName(Token name) {
        return arguments.stream().filter(a -> a.matches(name)).findFirst();
    }

    public void setArguments(NodeList<Argument> arguments){
        this.arguments = arguments;
        this.addAstChild(this.arguments);
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
    public BaseTypeList getArgumentTypes() {
        return new BaseTypeList(
                arguments.stream()
                .map(Argument::getType)
                .toList()
        );
    }

    @Override
    public void accept(NodeVisitor visitor, VisitorMemory memory){
        visitor.visit(this, memory);
    }

    @Override
    public List<Dependable> findDefinition(Variable variable) {
        final List<Dependable> definitions = new ArrayList<>(super.findDefinition(variable));

        if(arguments.stream().anyMatch(v -> v.getDefinition() != variable && v.matches(variable.getDefinitionToken()))){
            definitions.add(this);
        }

        return definitions;
    }

    public Optional<Argument> findArgument(Token name) {
        for(Argument current: arguments){
            if(current.matches(name)){
                return Optional.of(current);
            }
        }

        return Optional.empty();
    }
}
