package lu.uni.serval.ikora.analytics.violations;

import lu.uni.serval.ikora.model.SourceNode;

public class Violation {
    public enum Level{
        WARNING,
        ERROR
    }

    public enum Cause {
        MULTIPLE_DEFINITIONS,
        INFINITE_LOOP,
        LITERAL_LOCATOR,
        TRANSITIVE_DEPENDENCY,
        NO_DEFINITION_FOUND
    }

    private final Level level;
    private final SourceNode sourceNode;
    private final Cause cause;

    public Violation(Level level, SourceNode sourceNode, Cause cause) {
        this.level = level;
        this.sourceNode = sourceNode;
        this.cause = cause;
    }

    public Level getLevel() {
        return level;
    }

    public SourceNode getSourceNode() {
        return sourceNode;
    }

    public Cause getCause() {
        return cause;
    }
}
