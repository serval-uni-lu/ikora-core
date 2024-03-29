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

public class KeywordCall extends Step {
    private final Link<KeywordCall, Keyword> link;

    private NodeList<Argument> arguments;
    private Gherkin gherkin;

    public KeywordCall(Token name) {
        super(name);
        addToken(name);

        this.gherkin = Gherkin.none();
        this.link = new Link<>(this);

        this.arguments = new NodeList<>();
        this.addAstChild(this.arguments);
    }

    public void setGherkin(Gherkin gherkin) {
        this.gherkin = gherkin;
    }

    public Gherkin getGherkin(){
        return gherkin;
    }

    public void linkKeyword(Keyword keyword, Link.Import importLink) {
        if(keyword == null){
            throw new NullPointerException("Cannot link a keywordCall to a null Keyword");
        }

        link.addNode(keyword, importLink);
    }

    @Override
    public NodeList<Argument> getArgumentList() {
        return this.arguments;
    }

    public void setArgumentList(NodeList<Argument> arguments) {
        this.removeAstChild(this.arguments);
        this.arguments = arguments;
        this.arguments.stream().map(Argument::getTokens).forEach(this::addTokens);
        this.addAstChild(this.arguments);
    }

    @Override
    public boolean hasParameters() {
        return !this.arguments.isEmpty();
    }

    public Optional<Keyword> getKeyword() {
        return link.getNode();
    }

    public Keyword.Type getKeywordType(){
        return link.getNode().map(Keyword::getType).orElse(Keyword.Type.NONE);
    }

    public Set<Keyword> getAllPotentialKeywords(Link.Import importType){
        return link.getAllLinks(importType);
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

        if(other instanceof KeywordCall call){
            if(!this.getDefinitionToken().matches(call.getDefinitionToken())){
                edits.add(Edit.changeStepName(this, call));
            }

            List<Edit> argumentEdits = LevenshteinDistance.getDifferences(this.arguments, call.arguments);
            edits.addAll(argumentEdits);
        }
        else if(other instanceof Assignment assignment){
            edits.addAll(this.differences(assignment.getKeywordCall().orElse(null)));
            edits.addAll(LevenshteinDistance.getDifferences(new NodeList<>(), assignment.getLeftHandOperand()));
        }
        else{
            edits.add(Edit.changeType(this, other));
        }

        return edits;
    }

    @Override
    public String toString(){
        StringBuilder builder = new StringBuilder();

        builder.append(getDefinitionToken());

        for (Argument argument: getArgumentList()){
            builder.append("\t");
            builder.append(argument.toString());
        }

        return builder.toString();
    }

    @Override
    public void accept(NodeVisitor visitor, VisitorMemory memory){
        visitor.visit(this, memory);
    }

    @Override
    public Optional<KeywordCall> getKeywordCall() {
        return Optional.of(Objects.requireNonNullElse(template, this));
    }
}
