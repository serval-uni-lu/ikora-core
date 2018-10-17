package lu.uni.serval.robotframework.model;

import lu.uni.serval.utils.Differentiable;

public interface Element extends Differentiable {
    void setFile(TestCaseFile file);
    TestCaseFile getFile();
    String getFileName();

    void setLineRange(LineRange range);
    LineRange getLineRange();
    int getLoc();

    long getEpoch();

    Argument getNameAsArgument();

    boolean matches(String name);
}
