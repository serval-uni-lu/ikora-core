package tech.ikora.model;

import tech.ikora.analytics.Edit;

import java.util.List;

public interface Differentiable {
    double distance(Differentiable other);
    List<Edit> differences(Differentiable other);
}
