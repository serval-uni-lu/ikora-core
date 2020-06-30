package tech.ikora.analytics.visitor;

import tech.ikora.model.Node;
import tech.ikora.model.SourceNode;

public class FixedMemory implements VisitorMemory {
    private final int maxVisits;
    private final Class<? extends SourceNode> type;
    private int visitsCount;

    public FixedMemory(int maxVisits){
        this.maxVisits = maxVisits;
        this.visitsCount = 0;
        this.type = null;
    }

    public FixedMemory(int maxVisits, Class<? extends SourceNode> type){
        this.maxVisits = maxVisits;
        this.visitsCount = 0;
        this.type = type;
    }

    @Override
    public boolean isAcceptable(Node node) {
        return visitsCount < maxVisits;
    }

    @Override
    public VisitorMemory getUpdated(Node node) {
        if(type == null || type.isAssignableFrom(node.getClass()))
            ++visitsCount;

        return this;
    }
}
