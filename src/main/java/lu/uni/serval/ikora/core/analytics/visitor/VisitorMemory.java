package lu.uni.serval.ikora.core.analytics.visitor;

import lu.uni.serval.ikora.core.model.Node;

public interface VisitorMemory {
    boolean isAcceptable(Node node);
    VisitorMemory getUpdated(Node node);
}
