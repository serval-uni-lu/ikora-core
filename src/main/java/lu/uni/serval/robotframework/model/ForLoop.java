package lu.uni.serval.robotframework.model;

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
    public List<Keyword> getSequence() {
        List<Keyword> sequence = new ArrayList<>();

        for(Step step: steps){
            sequence.addAll(step.getSequence());
        }

        return sequence;
    }

    @Override
    public double difference(Differentiable<Step> other) {
        if(!(other instanceof ForLoop)){
            return 1;
        }

        ForLoop forLoop = (ForLoop)other;

        return LevenshteinDistance.index(steps, forLoop.steps);
    }
}
