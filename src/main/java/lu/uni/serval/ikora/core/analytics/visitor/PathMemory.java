package lu.uni.serval.ikora.core.analytics.visitor;

import lu.uni.serval.ikora.core.model.Node;
import lu.uni.serval.ikora.core.model.SourceNode;

import java.util.HashSet;
import java.util.Set;

public class PathMemory implements VisitorMemory {
    private final Set<Node> visited;

    public PathMemory(){
        visited = new HashSet<>();
    }

    @Override
    public VisitorMemory getUpdated(Node node) {
        add(node);
        return this;
    }

    @Override
    public boolean isAcceptable(Node node) {
        if(node == null){
            return false;
        }

        return !visited.contains(node);
    }

    protected void add(Node node){
        if(node == null){
            return;
        }

        if(!SourceNode.class.isAssignableFrom(node.getClass())){
            return;
        }

        visited.add(node);
    }
}
