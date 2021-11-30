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
import lu.uni.serval.ikora.core.analytics.visitor.NodeVisitor;
import lu.uni.serval.ikora.core.analytics.visitor.VisitorMemory;
import lu.uni.serval.ikora.core.runner.Runtime;
import lu.uni.serval.ikora.core.types.BaseType;
import lu.uni.serval.ikora.core.types.UnresolvedType;

import java.util.Collections;
import java.util.List;

public class Argument extends SourceNode implements HiddenAstNode {
    private final SourceNode definition;
    private final Token name;
    private BaseType type;
    private int position;

    public Argument(SourceNode definition, BaseType type, int position){
        if(definition == null){
            throw new NullPointerException("Argument cannot be initialize with null value");
        }

        this.definition = definition;
        this.name = definition.getDefinitionToken();
        addTokens(this.definition.getTokens());
        this.addAstChild(this.definition);

        this.type = type;
        this.position = position;
    }

    public Argument(SourceNode definition) {
        this(definition, UnresolvedType.get(), -1);
    }

    public SourceNode getDefinition() {
        return this.definition;
    }

    public boolean isScalarVariable(){
        return isClass(ScalarVariable.class);
    }

    public boolean isDictionaryVariable(){
        return isClass(DictionaryVariable.class);
    }

    public boolean isDictionaryEntry(){
        return isClass(DictionaryEntry.class);
    }

    public boolean isListVariable(){
        return isClass(ListVariable.class);
    }

    public boolean isLiteral(){
        return isClass(Literal.class);
    }

    public boolean isKeywordCall(){
        return isClass(KeywordCall.class);
    }

    public boolean isVariable(){
        return isClass(Variable.class);
    }

    public void setType(BaseType type) {
        this.type = type;
    }

    public BaseType getType() {
        return type;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    @Override
    public boolean matches(Token name) {
        return this.getDefinition().matches(name);
    }

    @Override
    public void accept(NodeVisitor visitor, VisitorMemory memory) {
        visitor.visit(this, memory);
    }

    @Override
    public void execute(Runtime runtime) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<Edit> differences(SourceNode other) {
        if(other == null){
            return Collections.singletonList(Edit.removeElement(this.getClass(), this));
        }

        if(other == this){
            return Collections.emptyList();
        }

        if(!(other instanceof Argument)){
            return Collections.singletonList(Edit.changeType(this, other));
        }

        return this.getDefinition().differences(((Argument) other).getDefinition());
    }

    @Override
    public Token getDefinitionToken() {
        return this.name;
    }

    @Override
    public String toString() {
        if(name != null){
            return name.toString();
        }

        return "<ARGUMENT>";
    }

    public boolean isType(Class<? extends BaseType> testType){
        return this.type.getClass().isAssignableFrom(testType);
    }

    private boolean isClass(Class<? extends SourceNode> clazz){
        return clazz.isAssignableFrom(this.getDefinition().getClass());
    }
}
