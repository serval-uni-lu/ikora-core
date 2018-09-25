package lu.uni.serval.robotframework.model;

import lu.uni.serval.utils.Differentiable;

public interface Element extends Differentiable {
    void setFile(TestCaseFile file);
    TestCaseFile getFile();
    String getFileName();

    long getEpoch();

    Argument getName();

    boolean matches(String name);
}
