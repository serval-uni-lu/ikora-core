package lu.uni.serval.ikora.core.analytics.difference;

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

import lu.uni.serval.ikora.core.model.SourceNode;
import lu.uni.serval.ikora.core.model.KeywordDefinition;

import java.util.*;

public class Difference {
    private final SourceNode left;
    private final SourceNode right;
    private final List<Edit> edits;

    private Difference(SourceNode left, SourceNode right){
        this.left = left;
        this.right = right;

        this.edits = new ArrayList<>();
    }

    public boolean isEmpty(){
        return edits.isEmpty();
    }

    public boolean isEmpty(Set<Edit.Type> ignore){
        if(edits.isEmpty()){
            return true;
        }

        if(ignore.isEmpty()){
            return isEmpty();
        }

        for(Edit edit : edits){
            if (!ignore.contains(edit.getType())){
                return false;
            }
        }

        return true;
    }

    public SourceNode getLeft(){
        return left;
    }

    public SourceNode getRight(){
        return right;
    }

    public SourceNode getValue(){
        if(left != null){
            return left;
        }

        return right;
    }

    public List<Edit> getEdits() {
        return edits;
    }

    public static Difference of(SourceNode before, SourceNode after) {
        Difference difference = new Difference(before, after);

        if(before == after){
            return difference;
        }

        if(before == null){
            difference.edits.add(Edit.addElement(after.getClass(), after));
            return difference;
        }

        if(after == null){
            difference.edits.add(Edit.removeElement(before.getClass(), before));
            return difference;
        }

        difference.edits.addAll(before.differences(after));

        return difference;
    }

    @Override
    public boolean equals(Object other) {
        if(this == other){
            return true;
        }

        if(other == null){
            return false;
        }

        if(this.getClass() != other.getClass()){
            return false;
        }

        Difference difference = (Difference)other;

        return this.left == difference.left
                && this.right == difference.right;
    }

    @Override
    public int hashCode(){
        int hash = 7;
        hash = getNodeHash(hash, left);
        hash = getNodeHash(hash, right);

        return hash;
    }

    private int getNodeHash(int hash, SourceNode element){
        if(element == null){
            hash = 31 * hash;
        }
        else if(element instanceof KeywordDefinition){
            KeywordDefinition keyword = (KeywordDefinition)element;

            hash = 31 * hash + (keyword.getSourceFile() == null ? 0 : keyword.getSourceFile().hashCode());
            hash = 31 * hash + (keyword.getNameToken() == null ? 0 : keyword.getNameToken().hashCode());
        }
        else{
            hash = element.hashCode();
        }

        return hash;
    }

    public Class<?> getType() {
        return this.getValue().getClass();
    }
}
