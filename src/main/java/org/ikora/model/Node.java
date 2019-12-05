package org.ikora.model;


import org.ikora.analytics.NodeVisitor;
import org.ikora.analytics.VisitorMemory;
import org.ikora.exception.InvalidDependencyException;

import javax.annotation.Nonnull;
import java.util.Set;

public interface Node extends Differentiable {
    void setFile(@Nonnull SourceFile file);
    SourceFile getFile();
    String getFileName();
    String getLibraryName();

    void setLineRange(@Nonnull LineRange range);
    LineRange getLineRange();
    int getLoc();

    long getEpoch();

    Value getNameAsValue();

    boolean isDeadCode();

    void accept(NodeVisitor visitor, VisitorMemory memory);

    boolean matches(@Nonnull String name);

    Set<Node> getDependencies();
    void addDependency(@Nonnull Node dependency) throws InvalidDependencyException;

    default Project getProject(){
        if(getFile() == null){
            return null;
        }

        return getFile().getProject();
    }
}
