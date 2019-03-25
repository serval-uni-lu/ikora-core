package org.ukwikora.model;

import java.util.Set;

public class StatementLink {
    private final Statement source;
    private Set<Statement> callees;
    private Set<Statement> dynamicCallees;

    public StatementLink(Statement source) {
        this.source = source;
    }

    public Statement getSource() {
        return source;
    }
}
