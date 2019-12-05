package org.ikora.analytics;

import org.ikora.model.Node;

public interface VisitorMemory {
    boolean isAcceptable(Node node);
    VisitorMemory getUpdated(Node node);
}
