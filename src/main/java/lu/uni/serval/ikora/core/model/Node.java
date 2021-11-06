package lu.uni.serval.ikora.core.model;

import lu.uni.serval.ikora.core.analytics.visitor.NodeVisitor;
import lu.uni.serval.ikora.core.analytics.visitor.VisitorMemory;
import lu.uni.serval.ikora.core.exception.RunnerException;
import lu.uni.serval.ikora.core.runner.Runtime;

public interface Node {
    String getName();
    String getLibraryName();

    boolean matches(Token name);
    void accept(NodeVisitor visitor, VisitorMemory memory);
    void execute(Runtime runtime) throws RunnerException;
}
