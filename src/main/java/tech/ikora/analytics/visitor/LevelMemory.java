package tech.ikora.analytics.visitor;

import tech.ikora.model.KeywordDefinition;
import tech.ikora.model.Node;
import tech.ikora.model.SourceNode;

import java.util.HashSet;
import java.util.Set;

public class LevelMemory implements VisitorMemory {
    private final Set<Node> visited;
    private int level;

    public LevelMemory() {
        this.visited = new HashSet<>();
        this.level = 0;
    }

    private LevelMemory(LevelMemory other) {
        this.visited = other.visited;
        this.level = other.level;
    }

    @Override
    public VisitorMemory getUpdated(Node node) {
        if(KeywordDefinition.class.isAssignableFrom(node.getClass())){
            return this;
        }

        if(!this.isAcceptable(node)){
            return this;
        }

        LevelMemory updated = new LevelMemory(this);
        updated.add(node);
        updated.level++;

        return updated;
    }

    @Override
    public boolean isAcceptable(Node node) {
        return !visited.contains(node);
    }

    protected void add(Node node){
        if(!SourceNode.class.isAssignableFrom(node.getClass())){
            return;
        }

        visited.add(node);
    }

    public int getLevel() {
        return level - 1;
    }
}
