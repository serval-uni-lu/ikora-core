package org.ukwikora.analytics;

import org.ukwikora.model.Statement;

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
    private final Statement statement;
    private final Cause cause;

    public Violation(Level level, Statement statement, Cause cause) {
        this.level = level;
        this.statement = statement;
        this.cause = cause;
    }

    public Level getLevel() {
        return level;
    }

    public Statement getStatement() {
        return statement;
    }

    public Cause getCause() {
        return cause;
    }
}
