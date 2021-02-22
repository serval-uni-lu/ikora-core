package lu.uni.serval.ikora.model;

import lu.uni.serval.ikora.analytics.visitor.NodeVisitor;
import lu.uni.serval.ikora.analytics.visitor.VisitorMemory;
import lu.uni.serval.ikora.runner.Runtime;

public interface Node {
    String getName();
    String getLibraryName();

    boolean matches(Token name);
    void accept(NodeVisitor visitor, VisitorMemory memory);
    void execute(Runtime runtime) throws Exception;
}
