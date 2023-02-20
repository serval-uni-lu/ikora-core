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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DictionaryEntry extends Value {
    private final SourceNode key;
    private final SourceNode value;

    public DictionaryEntry(SourceNode key, SourceNode value){
        addToken(key.getDefinitionToken());
        addToken(value.getDefinitionToken());

        addAstChild(key);
        addAstChild(value);

        this.key = key;
        this.value = value;
    }

    public SourceNode getKey(){
        return this.key;
    }

    public SourceNode getValue(){
        return this.value;
    }


    @Override
    public boolean matches(Token name) {
        return key.matches(name);
    }

    @Override
    public void accept(NodeVisitor visitor, VisitorMemory memory) {
        visitor.visit(this, memory);
    }

    @Override
    public Token getDefinitionToken() {
        return getKey().getDefinitionToken();
    }

    @Override
    public List<Edit> differences(SourceNode other) {
        if(other == null){
            return Collections.singletonList(Edit.removeElement(this.getClass(), this));
        }

        if(other == this){
            return Collections.emptyList();
        }

        if(other instanceof DictionaryEntry entry){
            final List<Edit> edits = new ArrayList<>();
            edits.addAll(this.key.differences(entry.key));
            edits.addAll(this.value.differences(entry.value));
            return edits;
        }

        return Collections.singletonList(Edit.changeValueType(this, other));
    }
}
