package tech.ikora.analytics.visitor;

import tech.ikora.model.KeywordDefinition;
import tech.ikora.model.Node;

public class LevelMemory extends PathMemory {
    private int level;

    public LevelMemory() {
        this.level = 0;
    }

    private LevelMemory(LevelMemory other) {
        super(other);
        this.level = other.level;
    }

    @Override
    public VisitorMemory getUpdated(Node node) {
        if(KeywordDefinition.class.isAssignableFrom(node.getClass())){
            return this;
        }

        LevelMemory updated = new LevelMemory(this);
        updated.add(node);
        updated.level++;

        return updated;
    }

    public int getLevel() {
        return level - 1;
    }
}
