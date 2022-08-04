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

import lu.uni.serval.ikora.core.analytics.difference.Edit;
import lu.uni.serval.ikora.core.analytics.resolver.ValueResolver;
import lu.uni.serval.ikora.core.utils.Ast;
import lu.uni.serval.ikora.core.utils.LevenshteinDistance;

import java.util.*;
import java.util.stream.Collectors;

public abstract class KeywordDefinition extends SourceNode implements Keyword, Iterable<Step>, Delayable, ScopeNode {
    private final Token name;
    private final NodeList<Step> steps;
    private final Set<SourceNode> dependencies;
    private Documentation documentation;
    private NodeList<Literal> tags;
    private TimeOut timeOut;

    private final List<Variable> localVariables;

    KeywordDefinition(Token name){
        this.dependencies = new HashSet<>();

        this.steps = new NodeList<>();
        this.addAstChild(this.steps);

        this.tags = new NodeList<>();
        this.addAstChild(this.tags);

        this.timeOut = TimeOut.none();
        this.addAstChild(this.timeOut);

        this.documentation = new Documentation();
        this.localVariables = new ArrayList<>();

        this.name = name;
    }

    public void addStep(Step step) {
        this.steps.add(step);
        addTokens(step.getTokens());
    }

    public void setTags(NodeList<Literal> tags) {
        this.removeAstChild(this.tags);
        this.tags = tags;
        this.addAstChild(this.tags);

        addTokens(this.tags.getTokens());
    }

    public void addLocalVariables(NodeList<Variable> variables){
        localVariables.addAll(variables);
    }

    public void addLocalVariable(Variable variable){
        localVariables.add(variable);
    }

    public NodeList<Literal> getTags() {
        return tags;
    }

    public Step getStep(int position) {
        if(steps.size() <= position  || 0 > position){
            return null;
        }

        return steps.get(position);
    }

    public <T> Set<T> getUsages(Class<T> type){
        return getDependencies().stream()
                .map(d -> Ast.getParentByType(d, type))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toSet());
    }

    @Override
    public TimeOut getTimeOut() {
        return timeOut;
    }

    @Override
    public void setTimeOut(TimeOut timeOut){
        this.removeAstChild(this.timeOut);
        this.timeOut = timeOut;
        this.addAstChild(this.timeOut);
    }

    @Override
    public String toString() {
        return String.format("%s - %s", getLibraryName(), getDefinitionToken());
    }

    public NodeList<Step> getSteps() {
        return steps;
    }

    public int getStepCount(){
        int count = 0;

        for(Step step: steps){
            count += step.getStepCount() + 1;
        }

        return count;
    }

    public void setDocumentation(Documentation documentation){
        this.documentation = documentation;
        addTokens(this.documentation.getTokens());
    }

    @Override
    public Token getDefinitionToken() {
        return name;
    }

    @Override
    public Documentation getDocumentation() {
        return documentation;
    }

    @Override
    public boolean matches(Token token) {
        if(token == null){
            return false;
        }

        return ValueResolver.matches(this.name, token);
    }

    @Override
    public Iterator<Step> iterator() {
        return steps.iterator();
    }

    @Override
    public List<Edit> differences(SourceNode other) {
        List<Edit> edits = new ArrayList<>();

        if(other == this){
            return edits;
        }

        if(other == null || !KeywordDefinition.class.isAssignableFrom(other.getClass())){
            edits.add(Edit.invalid(this, other));
            return edits;
        }

        KeywordDefinition that = (KeywordDefinition)other;

        // check name change
        if(!this.getDefinitionToken().matches(that.getDefinitionToken())){
            edits.add(Edit.changeName(this, other));
        }

        // check documentation change
        if(this.getDocumentation() == null && that.getDocumentation() != null){
            edits.add(Edit.addDocumentation(that.documentation));
        }
        else{
            edits.addAll(this.documentation.differences(that.documentation));
        }

        // check step changes
        List<Edit> stepEdits = LevenshteinDistance.getDifferences(this.getSteps(), that.getSteps());
        edits.addAll(stepEdits);

        return edits;
    }

    @Override
    public Type getType(){
        return Type.USER;
    }

    @Override
    public List<Dependable> findDefinition(Variable variable) {
        return this.steps.stream()
                .filter(Assignment.class::isInstance)
                .map(Assignment.class::cast)
                .filter(a -> a.isDefinition(variable))
                .map(Dependable.class::cast)
                .collect(Collectors.toList());
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
