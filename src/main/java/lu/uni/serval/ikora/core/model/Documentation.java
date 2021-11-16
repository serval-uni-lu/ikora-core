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
import lu.uni.serval.ikora.core.exception.RunnerException;
import lu.uni.serval.ikora.core.runner.Runtime;

import java.util.Collections;
import java.util.List;

public class Documentation extends SourceNode{
    private final Token label;
    private final Tokens text;

    public Documentation(){
        this.label = Token.empty();
        this.text = Tokens.empty();
    }

    public Documentation(Token label, Tokens text){
        this.label = label.setType(Token.Type.LABEL);
        this.text = text.setType(Token.Type.DOCUMENTATION);

        addToken(this.label);
        addTokens(this.text);
    }

    public Token getLabel() {
        return this.label;
    }

    public Tokens getText() {
        return this.text;
    }

    public boolean isPresent(){
        return !this.label.isEmpty();
    }

    public boolean isEmpty(){
        return isPresent() && this.text.isEmpty();
    }

    @Override
    public String toString(){
        return text.clean().toString();
    }

    @Override
    public boolean matches(Token name) {
        return this.text.first().equalsIgnorePosition(name);
    }

    @Override
    public void accept(NodeVisitor visitor, VisitorMemory memory) {
        visitor.visit(this, memory);
    }

    @Override
    public void execute(Runtime runtime) throws RunnerException {
        //nothing to do
    }

    @Override
    public Token getNameToken() {
        return text.first();
    }

    @Override
    public double distance(SourceNode other) {
        return 0;
    }

    @Override
    public List<Edit> differences(SourceNode other) {
        if(other == null){
            return Collections.singletonList(Edit.addDocumentation(this));
        }

        if(other instanceof Documentation){
            Documentation that = (Documentation) other;

            if(this.text.isEmpty() && !that.text.isEmpty()){
                return Collections.singletonList(Edit.addDocumentation(that));
            }
            else if(!this.text.isEmpty() && that.text.isEmpty()){
                return Collections.singletonList(Edit.removeDocumentation(this));
            }
            else if(!this.text.equalsIgnorePosition(that.text)){
                return Collections.singletonList(Edit.changeDocumentation(this, that));
            }
        }

        return Collections.emptyList();
    }
}
