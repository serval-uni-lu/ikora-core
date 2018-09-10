package lu.uni.serval.robotframework.model;

import lu.uni.serval.analytics.Action;
import lu.uni.serval.robotframework.runner.Runtime;
import lu.uni.serval.utils.Differentiable;
import lu.uni.serval.utils.LevenshteinDistance;

import java.util.ArrayList;
import java.util.List;

public class KeywordCall extends Step {
    private Keyword keyword;
    private List<Argument> parameters;

    public KeywordCall() {
        this.parameters = new ArrayList<>();
    }

    public void setKeyword(Keyword keyword){
        this.keyword = keyword;

        if(this.keyword != null) {
            this.keyword.addDependency(getParent());
        }
    }

    public void addParameter(String argument) {
        this.parameters.add(new Argument(argument));
    }

    public List<Argument> getParameters() {
        return this.parameters;
    }

    public Keyword getKeyword() {
        return keyword;
    }

    @Override
    public Keyword getStep(int position) {
        return keyword.getStep(position);
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof KeywordCall)){
            return false;
        }

        if(!super.equals(other)) {
            return false;
        }

        KeywordCall call = (KeywordCall)other;

        boolean same = this.parameters.size() == call.parameters.size();

        for(int i = 0; same && i < this.parameters.size(); ++i) {
            same &= this.parameters.get(i).equals(call.parameters.get(i));
        }

        return  same;
    }

    @Override
    public void execute(Runtime runtime) {

    }

    @Override
    public int getSize() {
        return this.keyword != null ? this.keyword.getSize() : 0;
    }

    @Override
    public List<Keyword> getSequence() {
        if(this.keyword == null){
            List<Keyword> sequence = new ArrayList<>();
            sequence.add(this);

            return sequence;
        }

        return this.keyword.getSequence();
    }

    @Override
    public double distance(Differentiable other) {
        if(!(other instanceof KeywordCall)){
            return 1;
        }

        KeywordCall call = (KeywordCall)other;

        double nameIndex = LevenshteinDistance.stringIndex(getName().toString(), call.getName().toString());
        double parameterIndex = LevenshteinDistance.index(getParameters(), call.getParameters());

        return (0.5 * nameIndex) + (0.5 * parameterIndex);
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
            KeywordCall call = (KeywordCall)other;

            if(!this.getName().toString().equalsIgnoreCase(call.toString())){
                actions.add(Action.changeStepName(this, call));
            }

            if(LevenshteinDistance.index(this.getParameters(), call.getParameters()) > 0){
                actions.add(Action.changeStepArguments(this, call));
            }
        }

        return actions;
    }

    @Override
    public String toString(){
        StringBuilder builder = new StringBuilder();

        builder.append(getName().toString());

        for (Argument parameter: parameters){
            builder.append("\t");
            builder.append(parameter.toString());
        }

        return builder.toString();
    }
}
