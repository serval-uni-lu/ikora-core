package lu.uni.serval.robotframework.model;

import lu.uni.serval.robotframework.analytics.StatusResults;

public interface Element extends StatusResults.Differentiable {
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
