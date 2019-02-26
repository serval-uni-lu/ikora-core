package org.ukwikora.analytics;

import org.ukwikora.model.Sequence;
import org.ukwikora.model.Statement;

public class KeywordStatistics {
    public static int getConnectivity(Statement statement){
        ConnectivityVisitor visitor = new ConnectivityVisitor();
        statement.accept(visitor, new PathMemory());

        return visitor.getConnectivity();
    }

    public static int getSize(Statement statement){
        SizeVisitor visitor = new SizeVisitor();
        statement.accept(visitor, new PathMemory());

        return visitor.getSize();
    }

    public static int getLevel(Statement statement){
        LevelVisitor visitor = new LevelVisitor();
        statement.accept(visitor, new LevelMemory());

        return visitor.getLevel();
    }

    public static int getSequenceSize(Statement statement){
        SequenceVisitor visitor = new SequenceVisitor();
        statement.accept(visitor, new PathMemory());

        return visitor.getSequenceSize();
    }

    public static Sequence getSequence(Statement statement){
        SequenceVisitor visitor = new SequenceVisitor();
        statement.accept(visitor, new PathMemory());

        return visitor.getSequence();
    }
}
