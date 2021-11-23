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
import lu.uni.serval.ikora.core.utils.Ast;
import lu.uni.serval.ikora.core.utils.FileUtils;

import java.util.*;

public abstract class SourceNode implements Node {
    private SourceNode astParent;
    private final List<SourceNode> astChildren;
    private final Tokens tokens;

    SourceNode(){
        astParent = null;
        astChildren = new ArrayList<>();
        tokens = new Tokens();
    }

    public String getName(){
        return getDefinitionToken().getText();
    }

    public SourceFile getSourceFile() {
        if(astParent == null){
            return null;
        }

        return astParent.getSourceFile();
    }

    protected void addAstChild(SourceNode astChild){
        if(astChild == null || astChildren.contains(astChild)){
            return;
        }

        astChild.astParent = this;
        astChildren.add(astChild);
    }

    protected void addAstChild(int index, SourceNode astChild) {
        if(astChild == null || astChildren.contains(astChild)){
            return;
        }

        astChild.astParent = this;
        astChildren.add(index, astChild);
    }

    protected void removeAstChild(SourceNode astChild){
        if(astChild == null || !astChildren.contains(astChild)){
            return;
        }

        astChild.astParent = null;
        astChildren.remove(astChild);
    }

    protected void clearAstChildren() {
        this.astChildren.clear();
    }

    public Source getSource(){
        if(getSourceFile() == null){
            return null;
        }

        return getSourceFile().getSource();
    }

    @Override
    public String getLibraryName() {
        if(this.getSourceFile() == null){
            return FileUtils.IN_MEMORY;
        }

        return this.getSourceFile().getName();
    }

    public long getEpoch() {
        if(getSourceFile() == null){
            return -1;
        }

        return getSourceFile().getEpoch();
    }

    public Project getProject(){
        if(getSourceFile() == null){
            return null;
        }

        return getSourceFile().getProject();
    }

    public SourceNode getAstParent() {
        return getAstParent(true);
    }

    public SourceNode getAstParent(boolean ignoreHiddenNodes){
        if(astParent == null){
            return null;
        }

        if(ignoreHiddenNodes && HiddenAstNode.class.isAssignableFrom(astParent.getClass())){
            return astParent.getAstParent();
        }

        return astParent;
    }

    public List<SourceNode> getAstChildren() {
        return astChildren;
    }

    public boolean isDeadCode(){
        final Set<SourceNode> dependencies = Ast.getParentByType(this, Dependable.class)
                .map(Dependable::getDependencies)
                .orElse(Collections.emptySet());

        return dependencies.isEmpty();
    }

    public void addToken(Token token){
        if(token == null || token.isEmpty()){
            return;
        }

        this.tokens.add(token);
    }

    public void addTokens(Tokens tokens){
        if(tokens == null){
            return;
        }

        this.tokens.addAll(tokens);
    }

    public Tokens getTokens(){
        return tokens;
    }

    public Range getRange(){
        return Range.fromTokens(tokens, null);
    }

    public int getLoc() {
        if(getSourceFile() == null){
            return -1;
        }

        int startLine = getRange().getStart().getLine();
        int endLine = getRange().getEnd().getLine();

        return this.getSourceFile().getLinesOfCode(startLine, endLine);
    }

    public abstract Token getDefinitionToken();
    public abstract double distance(SourceNode other);
    public abstract List<Edit> differences(SourceNode other);
}
