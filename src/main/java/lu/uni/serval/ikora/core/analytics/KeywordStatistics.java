package lu.uni.serval.ikora.core.analytics;

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
