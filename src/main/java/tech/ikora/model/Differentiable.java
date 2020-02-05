package tech.ikora.model;

import tech.ikora.analytics.Action;

import java.util.List;

public interface Differentiable {
    double distance(Differentiable other);
    List<Action> differences(Differentiable other);
}
