package tech.ikora.analytics.visitor;

import tech.ikora.model.Node;

public interface VisitorMemory {
    boolean isAcceptable(Node node);
    VisitorMemory getUpdated(Node node);
}
