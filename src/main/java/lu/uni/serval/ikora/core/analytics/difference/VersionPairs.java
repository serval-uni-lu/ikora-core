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
package lu.uni.serval.ikora.core.analytics.difference;

import lu.uni.serval.ikora.core.model.Projects;
import lu.uni.serval.ikora.core.model.SourceNode;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

public class VersionPairs {
    private final List<Pair<SourceNode, SourceNode>> pairs;
    private final Set<Edit> edits;
    Projects version1;
    Projects version2;

    VersionPairs(List<Pair<SourceNode, SourceNode>> pairs, Set<Edit> edits, Projects version1, Projects version2) {
        this.pairs = pairs;
        this.edits = edits;
        this.version1 = version1;
        this.version2 = version2;
    }

    public Optional<SourceNode> findPrevious(SourceNode node){
        if(node == null){
            throw new NullPointerException();
        }

        if(!version2.contains(node.getProject())){
            throw new IllegalArgumentException("Illegal version");
        }

        return pairs.stream()
                .filter(p -> p.getRight() == node)
                .map(Pair::getLeft)
                .filter(Objects::nonNull)
                .findAny();
    }

    public Set<Edit> getEdits(){
        return this.edits;
    }

    public Projects getLeftVersion() {
        return version1;
    }

    public Projects getRightVersion() {
        return version2;
    }
}
