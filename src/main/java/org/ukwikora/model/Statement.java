package org.ukwikora.model;


import org.ukwikora.analytics.VisitorMemory;
import org.ukwikora.exception.InvalidDependencyException;

import javax.annotation.Nonnull;
import java.util.Set;

public interface Statement extends Differentiable {
    void setFile(@Nonnull TestCaseFile file);
    TestCaseFile getFile();
    String getFileName();
    String getLibraryName();

    void setLineRange(@Nonnull LineRange range);
    LineRange getLineRange();
    int getLoc();

    long getEpoch();

    Value getNameAsValue();

    void accept(StatementVisitor visitor, VisitorMemory memory);

    boolean matches(@Nonnull String name);

    Set<Statement> getDependencies();
    void addDependency(@Nonnull Statement dependency) throws InvalidDependencyException;
}
