package org.ukwikora.analytics;

import org.ukwikora.model.Statement;

public interface VisitorMemory {
    boolean isAcceptable(Statement statement);
    VisitorMemory getUpdated(Statement statement);
}
