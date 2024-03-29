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

import lu.uni.serval.ikora.core.analytics.difference.Edit;
import lu.uni.serval.ikora.core.analytics.visitor.NodeVisitor;
import lu.uni.serval.ikora.core.analytics.visitor.VisitorMemory;
import lu.uni.serval.ikora.core.types.UnresolvedType;
import lu.uni.serval.ikora.core.utils.LevenshteinDistance;

import java.util.*;

public class VariableAssignment extends SourceNode implements Dependable{
    private final Variable variable;
    private final Token equalSign;
    private final NodeList<Argument> values;
    private final Set<SourceNode> dependencies;

    public VariableAssignment(Variable variable){
        this.variable = variable;
        this.equalSign = Token.empty();
        this.values = new NodeList<>();
        this.dependencies = new HashSet<>();
        this.addTokens(variable.getTokens());

        addAstChild(this.variable);
        addAstChild(this.values);
    }

    public VariableAssignment(Variable variable, List<Value> values){
        this.variable = variable;
        this.equalSign = Token.empty();
        this.values = new NodeList<>();
        this.dependencies = new HashSet<>();

        for(Value value: values){
            addValue(value);
        }
    }

    public VariableAssignment(Variable variable, Token equalSign){
        this.variable = variable;
        this.equalSign = equalSign;
        this.values = new NodeList<>();
        this.dependencies = new HashSet<>();
        this.addTokens(variable.getTokens());

        addAstChild(this.variable);
        addAstChild(this.values);
    }

    public void addValue(SourceNode value) {
        this.addTokens(value.getTokens());
        this.values.add(new Argument(value, UnresolvedType.get(), this.values.size()));
    }

    public Variable getVariable(){
        return variable;
    }

    public Token getEqualSign(){
        return this.equalSign;
    }

    public NodeList<Argument> getValues() {
        return this.values;
    }

    @Override
    public boolean matches(Token name) {
        return variable.matches(name);
    }

    @Override
    public void accept(NodeVisitor visitor, VisitorMemory memory) {
        visitor.visit(this, memory);
    }

    @Override
    public Token getDefinitionToken() {
        return variable.getDefinitionToken();
    }

    @Override
    public List<Edit> differences(SourceNode other) {
        if(other == null){
            return Collections.singletonList(Edit.removeElement(this.getClass(), this));
        }

        if(other == this){
            return Collections.emptyList();
        }

        if(!(other instanceof VariableAssignment)){
            return Collections.singletonList(Edit.changeType(this, other));
        }

        VariableAssignment assignment = (VariableAssignment)other;

        List<Edit> edits = new ArrayList<>();

        if(!this.getName().equals(assignment.getName())){
            edits.add(Edit.changeName(this, other));
        }

        edits.addAll(LevenshteinDistance.getDifferences(this.getValues(), assignment.getValues()));

        return edits;
    }

    @Override
    public void addDependency(SourceNode node) {
        if(node == null) {
            return;
        }

        this.dependencies.add(node);
    }

    @Override
    public void removeDependency(SourceNode node) {
        this.dependencies.remove(node);
    }

    @Override
    public Set<SourceNode> getDependencies() {
        return dependencies;
    }
}
