package lu.uni.serval.utils;

import lu.uni.serval.analytics.Action;

import java.util.List;

public interface Differentiable {
    double distance(Differentiable other);
    List<Action>  differences(Differentiable other);
    String getName();
}
