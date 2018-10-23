package org.ukwikora.model;

import org.ukwikora.analytics.Action;

import java.util.List;

public interface Differentiable {
    double distance(Differentiable other);
    List<Action> differences(Differentiable other);
    String getName();
}