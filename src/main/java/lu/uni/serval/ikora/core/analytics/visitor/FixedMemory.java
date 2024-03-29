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
package lu.uni.serval.ikora.core.analytics.visitor;

import lu.uni.serval.ikora.core.model.Node;
import lu.uni.serval.ikora.core.model.SourceNode;

public class FixedMemory implements VisitorMemory {
    private final int maxVisits;
    private final Class<? extends SourceNode> type;
    private int visitsCount;

    public FixedMemory(int maxVisits){
        this.maxVisits = maxVisits;
        this.visitsCount = 0;
        this.type = null;
    }

    public FixedMemory(int maxVisits, Class<? extends SourceNode> type){
        this.maxVisits = maxVisits;
        this.visitsCount = 0;
        this.type = type;
    }

    @Override
    public boolean isAcceptable(Node node) {
        if(type == null || type.isAssignableFrom(node.getClass()))
            return visitsCount < maxVisits;

        return true;
    }

    @Override
    public VisitorMemory getUpdated(Node node) {
        if(type == null || type.isAssignableFrom(node.getClass()))
            ++visitsCount;

        return this;
    }
}
