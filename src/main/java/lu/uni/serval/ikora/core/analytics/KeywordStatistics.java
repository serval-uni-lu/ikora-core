package lu.uni.serval.ikora.core.analytics;

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

import lu.uni.serval.ikora.core.analytics.visitor.*;
import lu.uni.serval.ikora.core.model.KeywordDefinition;
import lu.uni.serval.ikora.core.model.Node;
import lu.uni.serval.ikora.core.model.Sequence;
import lu.uni.serval.ikora.core.model.SourceNode;

import java.util.Set;

public class KeywordStatistics {
    private KeywordStatistics() {}

    public static int getConnectivity(SourceNode sourceNode){
        ConnectivityVisitor visitor = new ConnectivityVisitor();
        sourceNode.accept(visitor, new PathMemory());

        return visitor.getConnectivity();
    }

    public static SizeVisitor.Result getSize(SourceNode sourceNode){
        SizeVisitor visitor = new SizeVisitor();
        sourceNode.accept(visitor, new PathMemory());

        return visitor.getResult();
    }

    public static int getLevel(SourceNode sourceNode){
        LevelVisitor visitor = new LevelVisitor();
        sourceNode.accept(visitor, new LevelMemory());

        return visitor.getLevel();
    }

    public static int getSequenceSize(SourceNode sourceNode){
        SequenceVisitor visitor = new SequenceVisitor();
        sourceNode.accept(visitor, new PathMemory());

        return visitor.getSequenceSize();
    }

    public static int getStatementCount(SourceNode sourceNode){
        StatementCounterVisitor visitor = new StatementCounterVisitor();
        sourceNode.accept(visitor, new PathMemory());

        return visitor.getStatementCount();
    }

    public static Sequence getSequence(SourceNode sourceNode){
        SequenceVisitor visitor = new SequenceVisitor();
        sourceNode.accept(visitor, new PathMemory());

        return visitor.getSequence();
    }

    public static Set<KeywordDefinition> getDependencies(Node node){
        DependencyCollectorVisitor visitor = new DependencyCollectorVisitor();
        node.accept(visitor, new PathMemory());

        return visitor.getDependencies();
    }
}
