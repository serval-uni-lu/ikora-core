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
import lu.uni.serval.ikora.core.parser.TokenScanner;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class Documentation extends SourceNode{
    private final Token label;
    private final Tokens content;

    private String cachedText = null;

    public Documentation(){
        this.label = Token.empty();
        this.content = Tokens.empty();
    }

    public Documentation(Token label, Tokens text){
        this.label = label.setType(Token.Type.LABEL);
        this.content = text.setType(Token.Type.DOCUMENTATION);

        addToken(this.label);
        addTokens(this.content);
    }

    public Token getLabel() {
        return this.label;
    }

    public Tokens getContent() {
        return this.content;
    }

    public String getText() {
        if(cachedText == null){
            final Iterator<Token> iterator = TokenScanner.from(this.content)
                    .skipTypes(Token.Type.CONTINUATION)
                    .iterator();

            final StringBuilder builder = new StringBuilder();

            while (iterator.hasNext()){
                builder.append(iterator.next());
                if(iterator.hasNext()) builder.append(" ");
            }

            cachedText = builder.toString();
        }

        return cachedText;
    }

    public boolean isPresent(){
        return !this.label.isEmpty();
    }

    public boolean isEmpty(){
        return isPresent() && this.content.isEmpty();
    }

    @Override
    public String toString(){
        return content.clean().toString();
    }

    @Override
    public boolean matches(Token name) {
        return this.content.first().matches(name);
    }

    @Override
    public void accept(NodeVisitor visitor, VisitorMemory memory) {
        visitor.visit(this, memory);
    }

    @Override
    public Token getDefinitionToken() {
        return content.first();
    }

    @Override
    public List<Edit> differences(SourceNode other) {
        if(other == null){
            return Collections.singletonList(Edit.addDocumentation(this));
        }

        if(other instanceof Documentation that){
            if(this.content.isEmpty() && !that.content.isEmpty()){
                return Collections.singletonList(Edit.addDocumentation(that));
            }
            else if(!this.content.isEmpty() && that.content.isEmpty()){
                return Collections.singletonList(Edit.removeDocumentation(this));
            }
            else if(!this.content.equalsIgnorePosition(that.content)){
                return Collections.singletonList(Edit.changeDocumentation(this, that));
            }
        }

        return Collections.emptyList();
    }
}
