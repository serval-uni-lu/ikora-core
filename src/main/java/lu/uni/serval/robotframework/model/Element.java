package lu.uni.serval.robotframework.model;

import lu.uni.serval.utils.Differentiable;

public interface Element extends Differentiable {
    void setFile(String file);
    String getFile();

    Argument getName();

    boolean matches(String name);
}
