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
import org.apache.commons.lang3.NotImplementedException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DictionaryEntry extends Value {
    private final SourceNode key;
    private final SourceNode value;

    public DictionaryEntry(SourceNode key, SourceNode value){
        addToken(key.getDefinitionToken());
        addToken(value.getDefinitionToken());

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
        return false;
    }

    @Override
    public void accept(NodeVisitor visitor, VisitorMemory memory) {
        //nothing to do on leaf node
    }

    @Override
    public void execute(Runtime runtime) throws RunnerException {
        throw new NotImplementedException("Runner is not implemented yet");
    }

    @Override
    public Token getDefinitionToken() {
        return getKey().getDefinitionToken();
    }

    @Override
    public double distance(SourceNode other) {
        if(this == other){
            return 0.;
        }

        if(other == null){
            return 1.;
        }

        if(DictionaryEntry.class.isAssignableFrom(other.getClass())){
            double distance = this.getKey().distance(((DictionaryEntry)other).getKey()) / 2;
            distance += this.getValue().distance(((DictionaryEntry)other).getValue()) / 2;

            return distance;
        }

        return 1.;
    }

    @Override
    public List<Edit> differences(SourceNode other) {
        if(other == null){
            return Collections.singletonList(Edit.removeElement(this.getClass(), this));
        }

        if(other == this){
            return Collections.emptyList();
        }

        if(!(other instanceof DictionaryEntry)){
            return Collections.singletonList(Edit.changeValueType(this, other));
        }

        DictionaryEntry entry = (DictionaryEntry)other;

        List<Edit> edits = new ArrayList<>();
        edits.addAll(this.key.differences(entry.key));
        edits.addAll(this.value.differences(entry.value));

        return edits;
    }
}
