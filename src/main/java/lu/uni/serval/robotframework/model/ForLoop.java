package lu.uni.serval.robotframework.model;

import lu.uni.serval.analytics.Action;
import lu.uni.serval.robotframework.runner.Runtime;
import lu.uni.serval.utils.Differentiable;
import lu.uni.serval.utils.LevenshteinDistance;

import java.util.ArrayList;
import java.util.List;

public class ForLoop extends Step {
    private List<Step> steps;
    private List<Argument> parameters;

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
    public List<Argument> getParameters() {
        return parameters;
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
    public void execute(Runtime runtime) {

    }

    @Override
    public int getSize() {
        int size = 1;

        for(Step step: steps){
            size += step.getSize();
        }

        return size;
    }

    @Override
    public int getDepth(){
        int depth = 0;

        for(Step step: steps){
            depth = Math.max(step.getSize(), depth);
        }

        return depth;
    }

    @Override
    public void getSequences(List<Sequence> sequences) {
        for(Step step: steps){
            step.getSequences(sequences);
        }
    }

    @Override
    public double distance(Differentiable other) {
        if(!(other instanceof ForLoop)){
            return 1;
        }

        ForLoop forLoop = (ForLoop)other;

        return LevenshteinDistance.index(steps, forLoop.steps);
    }

    @Override
    public List<Action> differences(Differentiable other) {
        List<Action> actions = new ArrayList<>();

        if(!(other instanceof Step)){
            return actions;
        }

        if(this.getClass() != other.getClass()){
            actions.add(Action.changeStepType(this, other));
        }
        else{
            ForLoop forLoop = (ForLoop)other;

            if(LevenshteinDistance.stringIndex(this.getName().toString(), forLoop.getName().toString()) > 0){
                actions.add(Action.changeForLoopCondition(this, forLoop));
            }

            if(LevenshteinDistance.index(this.getSteps(), forLoop.getSteps()) > 0){
                actions.add(Action.changeForLoopBody(this, forLoop));
            }
        }

        return actions;
    }
}
