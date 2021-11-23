package lu.uni.serval.ikora.core.analytics.visitor;

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

import lu.uni.serval.ikora.core.model.KeywordDefinition;
import lu.uni.serval.ikora.core.model.Node;
import lu.uni.serval.ikora.core.model.SourceNode;

import java.util.HashSet;
import java.util.Set;

public class LevelMemory implements VisitorMemory {
    private final Set<Node> visited;
    private int level;

    public LevelMemory() {
        this.visited = new HashSet<>();
        this.level = 0;
    }

    private LevelMemory(LevelMemory other) {
        this.visited = other.visited;
        this.level = other.level;
    }

    @Override
    public VisitorMemory getUpdated(Node node) {
        if(KeywordDefinition.class.isAssignableFrom(node.getClass())){
            return this;
        }

        if(!this.isAcceptable(node)){
            return this;
        }

        LevelMemory updated = new LevelMemory(this);
        updated.add(node);
        updated.level++;

        return updated;
    }

    @Override
    public boolean isAcceptable(Node node) {
        return !visited.contains(node);
    }

    protected void add(Node node){
        if(!SourceNode.class.isAssignableFrom(node.getClass())){
            return;
        }

        visited.add(node);
    }

    public int getLevel() {
        return level - 1;
    }
}
