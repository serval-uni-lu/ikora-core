package tech.ikora.analytics;

import tech.ikora.model.Node;

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
    private final Node node;
    private final Cause cause;

    public Violation(Level level, Node node, Cause cause) {
        this.level = level;
        this.node = node;
        this.cause = cause;
    }

    public Level getLevel() {
        return level;
    }

    public Node getNode() {
        return node;
    }

    public Cause getCause() {
        return cause;
    }
}
