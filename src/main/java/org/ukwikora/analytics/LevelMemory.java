package org.ukwikora.analytics;

import org.ukwikora.model.Statement;

public class LevelMemory extends PathMemory {
    private int depth;

    public LevelMemory() {
        this.depth = 0;
    }

    private LevelMemory(LevelMemory other) {
        super(other);
        this.depth = other.depth;
    }

    @Override
    public VisitorMemory getUpdated(Statement statement) {
        LevelMemory updated = new LevelMemory(this);
        updated.depth++;

        return updated;
    }

    public int getDepth() {
        return depth;
    }
}
