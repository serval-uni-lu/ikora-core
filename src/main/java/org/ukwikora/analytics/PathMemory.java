package org.ukwikora.analytics;

import org.ukwikora.model.Statement;

import java.util.HashSet;
import java.util.Set;

public class PathMemory implements VisitorMemory{
    private Set<Statement> visited;

    public PathMemory(){
        visited = new HashSet<>();
    }

    protected PathMemory(PathMemory other){
        this.visited = other.visited;
    }

    @Override
    public VisitorMemory getUpdated(Statement statement) {
        PathMemory updated = new PathMemory(this);
        updated.visited.add(statement);

        return updated;
    }

    @Override
    public boolean isAcceptable(Statement statement) {
        return !visited.contains(statement);
    }
}
