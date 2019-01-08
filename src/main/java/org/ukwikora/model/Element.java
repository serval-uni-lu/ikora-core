package org.ukwikora.model;


import javax.annotation.Nonnull;

public interface Element extends Differentiable {
    void setFile(@Nonnull TestCaseFile file);
    TestCaseFile getFile();
    String getFileName();

    void setLineRange(@Nonnull LineRange range);
    LineRange getLineRange();
    int getLoc();

    long getEpoch();

    Value getNameAsArgument();

    boolean matches(@Nonnull String name);
}
