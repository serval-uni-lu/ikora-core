package tech.ikora.analytics.visitor;

import tech.ikora.model.Node;
import tech.ikora.model.SourceNode;

import java.util.HashSet;
import java.util.Set;

public class PathMemory implements VisitorMemory {
    private final Set<Node> visited;

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
        if(!SourceNode.class.isAssignableFrom(node.getClass())){
            return;
        }

        visited.add(node);
    }

    @Override
    public boolean isAcceptable(Node node) {
        return !visited.contains(node);
    }
}
