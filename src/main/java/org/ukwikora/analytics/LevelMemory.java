package org.ukwikora.analytics;

import org.ukwikora.model.Statement;

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
    public VisitorMemory getUpdated(Statement statement) {
        LevelMemory updated = new LevelMemory(this);
        updated.level++;

        return updated;
    }

    public int getLevel() {
        return level;
    }
}