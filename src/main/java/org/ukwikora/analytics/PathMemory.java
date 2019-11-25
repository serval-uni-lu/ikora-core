package org.ukwikora.analytics;

import org.ukwikora.model.Node;

import java.util.HashSet;
import java.util.Set;

public class PathMemory implements VisitorMemory{
    private Set<Node> visited;

    public PathMemory(){
        visited = new HashSet<>();
    }

    protected PathMemory(PathMemory other){
        this.visited = other.visited;
    }

    @Override
    public VisitorMemory getUpdated(Node node) {
        PathMemory updated = new PathMemory(this);
        updated.add(node);

        return updated;
    }

    protected void add(Node node){
        visited.add(node);
    }

    @Override
    public boolean isAcceptable(Node node) {
        return !visited.contains(node);
    }
}
