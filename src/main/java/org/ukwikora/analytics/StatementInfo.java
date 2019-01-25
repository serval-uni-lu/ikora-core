package org.ukwikora.analytics;

import org.ukwikora.model.Project;

public class StatementInfo<T> {
    final private Project project;
    final private T statement;

    StatementInfo(Project project, T statement){
        this.project = project;
        this.statement = statement;
    }

    public T getStatement() {
        return statement;
    }

    public Project getProject() {
        return project;
    }
}
