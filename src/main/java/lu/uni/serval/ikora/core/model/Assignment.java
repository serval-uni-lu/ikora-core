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
import lu.uni.serval.ikora.core.types.KeywordType;
import lu.uni.serval.ikora.core.types.UnresolvedType;
import lu.uni.serval.ikora.core.utils.LevenshteinDistance;

import java.util.*;

public class Assignment extends Step implements Dependable {
    private final NodeList<Variable> leftHandOperand;
    private final Argument rightHandOperand;
    private final Set<SourceNode> dependencies;
    private final Token equalSign;

    public Assignment(Token name, List<Variable> leftHandOperand, KeywordCall rightHandOperand, Token equalSign) {
        super(name);

        this.dependencies = new HashSet<>();

        this.equalSign = equalSign;
        if(!equalSign.isEmpty()){
            this.addToken(equalSign);
        }

        this.leftHandOperand = new NodeList<>();
        this.addAstChild(this.leftHandOperand);

        for(Variable returnVariable: leftHandOperand){
            this.addReturnVariable(returnVariable);
        }

        if(rightHandOperand != null){
            this.rightHandOperand = new Argument(rightHandOperand, new KeywordType("Assignment"), 0);
            this.addAstChild(this.rightHandOperand);

            this.addTokens(this.rightHandOperand.getTokens());
        }
        else{
            this.rightHandOperand = null;
        }
    }

    public void addReturnVariable(Variable variable)  {
        if(variable == null){
            return;
        }

        this.addAstChild(variable);
        this.addTokens(variable.getTokens());

        leftHandOperand.add(variable);
    }

    public Token getEqualSign() {
        return this.equalSign;
    }

    public NodeList<Variable> getLeftHandOperand() {
        return leftHandOperand;
    }

    @Override
    public NodeList<Argument> getArgumentList() {
        return getKeywordCall().map(KeywordCall::getArgumentList).orElse(new NodeList<>());
    }

    @Override
    public boolean hasParameters() {
        return getKeywordCall().map(KeywordCall::hasParameters).orElse(false);
    }

    @Override
    public Optional<KeywordCall> getKeywordCall(){
        if(rightHandOperand == null){
            return Optional.empty();
        }

        if(rightHandOperand.isType(KeywordType.class) || rightHandOperand.isType(UnresolvedType.class)){
            return Optional.of((KeywordCall) rightHandOperand.getDefinition());
        }

        return Optional.empty();
    }

    @Override
    public List<Edit> differences(SourceNode other) {
        if(other == null){
            return Collections.singletonList(Edit.removeElement(this.getClass(), this));
        }

        if(other == this){
            return Collections.emptyList();
        }

        List<Edit> edits = new ArrayList<>();

        if(other instanceof Assignment assignment){
            edits.addAll(LevenshteinDistance.getDifferences(this.leftHandOperand, assignment.leftHandOperand));
            getKeywordCall().ifPresent(call -> edits.addAll(call.differences(assignment.getKeywordCall().orElse(null))));
        }
        else if(other instanceof KeywordCall){
            edits.addAll(LevenshteinDistance.getDifferences(this.leftHandOperand, new NodeList<>()));
            getKeywordCall().ifPresent(call -> edits.addAll(call.differences(other)));
        }
        else {
            edits.add(Edit.changeType(this, other));
        }

        return edits;
    }

    @Override
    public String toString(){
        StringBuilder builder = new StringBuilder();

        for (Variable variable: leftHandOperand){
            builder.append(variable.getDefinitionToken());
            builder.append("\t");
        }

        builder.append("\t=\t");

        getKeywordCall().ifPresent(builder::append);

        return builder.toString();
    }

    @Override
    public void accept(NodeVisitor visitor, VisitorMemory memory){
        visitor.visit(this, memory);
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

    public boolean isDefinition(Variable variable){
        for(Variable candidate: leftHandOperand){
            if(candidate.matches(variable.getDefinitionToken())){
                return true;
            }
        }

        return false;
    }
}
