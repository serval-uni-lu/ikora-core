package org.ukwikora.model;

import org.ukwikora.analytics.Action;
import org.ukwikora.analytics.VisitorMemory;
import org.ukwikora.utils.LevenshteinDistance;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class ForLoop extends Step {
    private List<Step> steps;
    private List<Value> parameters;

    public ForLoop() {
        steps = new ArrayList<>();
        parameters = new ArrayList<>();
    }

    public void addStep(Step step) {
        steps.add(step);
    }

    @Override
    public Keyword getStep(int position) {
        if(steps.size() <= position){
            return null;
        }

        return steps.get(position);
    }

    public List<Step> getSteps(){
        return steps;
    }

    @Override
    public List<Value> getParameters() {
        return parameters;
    }

    @Override
    public Optional<Value> getParameter(int position, boolean resolved) {
        return Optional.empty();
    }

    @Override
    public boolean hasParameters() {
        return false;
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof ForLoop)){
            return false;
        }

        if(!super.equals(other)) {
            return false;
        }

        ForLoop forLoop = (ForLoop)other;

        boolean same = this.steps.size() == forLoop.steps.size();

        for(int i = 0; same && i < this.steps.size(); ++i) {
            same &= this.steps.get(i).equals(forLoop.steps.get(i));
        }

        return  same;
    }

    @Override
    public boolean hasKeywordParameters() {
        return false;
    }

    @Override
    public List<KeywordCall> getKeywordParameter() {
        return Collections.emptyList();
    }

    @Override
    public void execute(Runtime runtime) {

    }

    @Override
    public int getLevel(){
        int depth = 0;

        for(Step step: steps){
            depth = Math.max(step.getLevel(), depth);
        }

        return depth;
    }

    @Override
    public int[] getKeywordsLaunchedPosition() {
        return new int[0];
    }

    @Override
    public void getSequences(List<Sequence> sequences) {
        for(Step step: steps){
            step.getSequences(sequences);
        }
    }

    @Override
    public double distance(@Nonnull Differentiable other) {
        if(!(other instanceof ForLoop)){
            return 1;
        }

        ForLoop forLoop = (ForLoop)other;

        return LevenshteinDistance.index(steps, forLoop.steps);
    }

    @Override
    public List<Action> differences(@Nonnull Differentiable other) {
        List<Action> actions = new ArrayList<>();

        if(!(other instanceof Step)){
            return actions;
        }

        if(this.getClass() != other.getClass()){
            actions.add(Action.changeStepType(this, other));
        }
        else{
            ForLoop forLoop = (ForLoop)other;

            if(LevenshteinDistance.stringIndex(this.getName(), forLoop.getName()) > 0){
                actions.add(Action.changeForLoopCondition(this, forLoop));
            }

            if(LevenshteinDistance.index(this.getSteps(), forLoop.getSteps()) > 0){
                actions.add(Action.changeForLoopBody(this, forLoop));
            }
        }

        return actions;
    }

    @Override
    public void accept(StatementVisitor visitor, VisitorMemory memory){
        visitor.visit(this, memory);
    }
}
