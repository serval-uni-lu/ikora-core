package org.ukwikora.analytics;

import org.ukwikora.model.Statement;

import java.util.Set;

public class Violation {
    public enum Level{
        WARNING,
        ERROR
    }

    public enum Cause {
        DOUBLE_DEFINITION,
        INFINITE_LOOP,
        LITERAL_LOCATOR
    }

    private final Level level;
    private final Set<Statement> statements;
    private final Cause cause;

    public Violation(Level level, Set<Statement> statements, Cause cause) {
        this.level = level;
        this.statements = statements;
        this.cause = cause;
    }

    public Level getLevel() {
        return level;
    }

    public Set<Statement> getStatements() {
        return statements;
    }

    public Cause getCause() {
        return cause;
    }
}
