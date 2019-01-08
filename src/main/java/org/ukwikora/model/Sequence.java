package org.ukwikora.model;

import org.ukwikora.analytics.Action;
import org.ukwikora.analytics.StatusResults;
import org.ukwikora.utils.LevenshteinDistance;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class Sequence implements Differentiable {
    private List<Keyword> steps;

    public Sequence(){
        this.steps = new ArrayList<>();
    }

    public void addStep(Keyword step){
        steps.add(step);
    }

    public int size(){
        return steps.size();
    }

    @Override
    public double distance(@Nonnull Differentiable other) {
        List<Action> actions = differences(other);

        for(Action action: actions){
            if(action.getType() == Action.Type.INVALID){
                return 1.0;
            }
        }

        return (double)actions.size() / this.size();
    }

    @Override
    public List<Action> differences(@Nonnull Differentiable other) {
        List<Action> actions = new ArrayList<>();

        if(other == this){
            return actions;
        }

        if(other.getClass() != this.getClass()){
            actions.add(Action.invalid(this, other));
            return actions;
        }

        Sequence sequence = (Sequence)other;

        actions.addAll(LevenshteinDistance.getDifferences(this.steps, sequence.steps));

        return actions;
    }

    @Override
    public String getName() {
        return String.valueOf(hashCode());
    }
}
