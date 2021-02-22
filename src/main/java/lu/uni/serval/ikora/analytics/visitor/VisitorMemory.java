package lu.uni.serval.ikora.analytics.visitor;

import lu.uni.serval.ikora.model.Node;

public interface VisitorMemory {
    boolean isAcceptable(Node node);
    VisitorMemory getUpdated(Node node);
}
