package org.ikora.model;

import org.ikora.analytics.Action;

import javax.annotation.Nonnull;
import java.util.List;

public interface Differentiable {
    double distance(@Nonnull Differentiable other);
    List<Action> differences(@Nonnull Differentiable other);
    String getName();
}
