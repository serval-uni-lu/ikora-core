package org.ukwikora.analytics;

import org.ukwikora.model.Statement;

public class DepthMemory extends PathMemory {
    private int depth;

    public DepthMemory() {
        this.depth = 0;
    }

    private DepthMemory(DepthMemory other) {
        super(other);
        this.depth = other.depth;
    }

    @Override
    public VisitorMemory getUpdated(Statement statement) {
        DepthMemory updated = new DepthMemory(this);
        updated.depth++;

        return updated;
    }

    public int getDepth() {
        return depth;
    }
}
