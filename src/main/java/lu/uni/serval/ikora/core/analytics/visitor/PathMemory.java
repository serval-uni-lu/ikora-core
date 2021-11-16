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

import lu.uni.serval.ikora.core.model.Node;
import lu.uni.serval.ikora.core.model.SourceNode;

import java.util.HashSet;
import java.util.Set;

public class PathMemory implements VisitorMemory {
    private final Set<Node> visited;

    public PathMemory(){
        visited = new HashSet<>();
    }

    @Override
    public VisitorMemory getUpdated(Node node) {
        add(node);
        return this;
    }

    @Override
    public boolean isAcceptable(Node node) {
        if(node == null){
            return false;
        }

        return !visited.contains(node);
    }

    protected void add(Node node){
        if(node == null){
            return;
        }

        if(!SourceNode.class.isAssignableFrom(node.getClass())){
            return;
        }

        visited.add(node);
    }
}
