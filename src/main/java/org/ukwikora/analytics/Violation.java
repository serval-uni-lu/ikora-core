package org.ukwikora.analytics;

import org.ukwikora.model.Statement;

public class Violation {
    public enum Level{
        WARNING, ERROR
    }

    private final Level level;
    private final Statement statement;
    private final String message;

    public Violation(Level level, Statement statement, String message) {
        this.level = level;
        this.statement = statement;
        this.message = message;
    }

    public Level getLevel() {
        return level;
    }

    public Statement getStatement() {
        return statement;
    }

    public String getMessage() {
        return message;
    }
}
