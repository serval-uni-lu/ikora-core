package org.ukwikora.model;

public interface Element extends Differentiable {
    void setFile(TestCaseFile file);
    TestCaseFile getFile();
    String getFileName();

    void setLineRange(LineRange range);
    LineRange getLineRange();
    int getLoc();

    long getEpoch();

    Value getNameAsArgument();

    boolean matches(String name);
}
