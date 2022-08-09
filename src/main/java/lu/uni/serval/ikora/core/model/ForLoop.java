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
import lu.uni.serval.ikora.core.utils.LevenshteinDistance;

import java.util.*;
import java.util.stream.Collectors;

public class ForLoop extends Step implements Dependable, ScopeNode {
    private final NodeList<Step> steps;
    private final Variable iterator;
    private final SourceNode interval;
    private final Set<SourceNode> dependencies;
    private final Token forToken;
    private final Token inToken;


    protected List<Variable> localVariables;

    public ForLoop(Token forToken, Token inToken, Variable iterator, SourceNode interval, List<Step> steps) {
        super(forToken);

        this.iterator = iterator;
        this.addAstChild(this.iterator);

        this.interval = interval;
        this.addAstChild(this.interval);

        this.addTokens(this.iterator.getTokens());
        this.addTokens(this.interval.getTokens());

        this.localVariables = new ArrayList<>();
        this.dependencies = new HashSet<>();


        this.steps = new NodeList<>(steps);
        this.addAstChild(this.steps);

        this.forToken = forToken;
        this.addToken(forToken);
        this.inToken = inToken;
        this.addToken(inToken);
    }

    public Token getForToken() {
        return forToken;
    }

    public Token getInToken() {
        return inToken;
    }

    public Variable getIterator() {
        return this.iterator;
    }

    public SourceNode getInterval() {
        return this.interval;
    }

    @Override
    public NodeList<Step> getSteps(){
        return this.steps;
    }

    @Override
    public NodeList<Argument> getArgumentList() {
        return new NodeList<>();
    }

    @Override
    public boolean hasParameters() {
        return false;
    }

    @Override
    public Optional<KeywordCall> getKeywordCall() {
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

        if(!this.getClass().isAssignableFrom(other.getClass())){
            edits.add(Edit.changeStep(this, other));
            return edits;
        }

        ForLoop forLoop = (ForLoop)other;

        edits.addAll(this.iterator.differences(forLoop.iterator));
        edits.addAll(this.interval.differences(forLoop.interval));
        edits.addAll(LevenshteinDistance.getDifferences(this.steps, forLoop.steps));

        return edits;
    }

    @Override
    public void accept(NodeVisitor visitor, VisitorMemory memory){
        visitor.visit(this, memory);
    }

    @Override
    public void setTemplate(KeywordCall template) {
        super.setTemplate(template);

        for(Step step: steps){
            step.setTemplate(template);
        }
    }

    @Override
    public List<Dependable> findDefinition(Variable variable) {
        final List<Dependable> definitions = this.steps.stream()
                .filter(Assignment.class::isInstance)
                .map(Assignment.class::cast)
                .filter(a -> a.isDefinition(variable))
                .map(Dependable.class::cast)
                .collect(Collectors.toList());

        if(iterator.matches(variable.getDefinitionToken())){
            definitions.add(this);
        }


        return definitions;
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
