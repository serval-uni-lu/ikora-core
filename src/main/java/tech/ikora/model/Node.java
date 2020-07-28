package tech.ikora.model;

import tech.ikora.analytics.visitor.NodeVisitor;
import tech.ikora.analytics.visitor.VisitorMemory;
import tech.ikora.runner.Runtime;

import java.util.Set;

public interface Node {
    String getName();
    String getLibraryName();

    boolean matches(Token name);
    void accept(NodeVisitor visitor, VisitorMemory memory);
    void execute(Runtime runtime) throws Exception;
}
