package lu.uni.serval.robotframework.model;

import lu.uni.serval.analytics.Action;
import lu.uni.serval.robotframework.runner.Runtime;
import lu.uni.serval.utils.Differentiable;
import lu.uni.serval.utils.LevenshteinDistance;

import java.util.*;

public class KeywordCall extends Step {
    private Keyword keyword;
    private List<Argument> parameters;
    private Map<Argument, KeywordCall> stepParameters;

    public KeywordCall() {
        this.parameters = new ArrayList<>();
        this.stepParameters = new HashMap<>();
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
        if(this.keyword == null){
            return 0;
        }
        else if(stepParameters.size() > 0){
            int size = 0;

            for (Step step: stepParameters.values()){
                if(step == null){
                    continue;
                }

                size += step.getSize();
            }

            return size;
        }

        return this.keyword.getSize();
    }

    @Override
    public void getSequences(List<List<Keyword>> sequences) {
        if(this.keyword == null){
            return;
        }
        else if(this.keyword.isAction() || this.keyword.isSynchronisation()){
            for(List<Keyword> sequence: sequences){
                sequence.add(this);
            }
        }
        else if(this.keyword.isCall()){
            for(KeywordCall step: stepParameters.values()){
                step.getSequences(sequences);
            }
        }
        else if(this.keyword.isControlFlow()) {
            List<List<Keyword>> alternates = new ArrayList<>();

            for(List<Keyword> sequence: sequences) {
                List<Keyword> alternate = new ArrayList<>();
                for(Keyword element: sequence){
                    alternate.add(element);
                }
                alternates.add(alternate);
            }

            for(KeywordCall step: stepParameters.values()){
                step.getSequences(alternates);
            }

            sequences.addAll(alternates);
        }
        else if(this.keyword instanceof KeywordDefinition){
            ((KeywordDefinition)keyword).getSequences(sequences);
        }
    }

    @Override
    public Argument.Type[] getArgumentTypes() {
        if(this.keyword == null){
            return new Argument.Type[0];
        }

        return this.keyword.getArgumentTypes();
    }

    @Override
    public int[] getKeywordsLaunchedPosition() {
        if(this.keyword == null){
            return new int[0];
        }

        int[] positions = this.keyword.getKeywordsLaunchedPosition();

        if(positions.length == 1 && positions[0] == -1){
            positions = new int[getParameters().size()];
            for (int i = 0; i < positions.length; ++i) {
                positions[i] = i;
            }
        }

        return positions;
    }

    @Override
    public boolean isAction() {
        return false;
    }

    @Override
    public boolean isControlFlow() {
        return false;
    }

    @Override
    public boolean isCall() {
        return false;
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

    public Step setKeywordParameter(Argument keywordParameter, Keyword keyword) {
        int index = parameters.indexOf(keywordParameter);

        if(index < 0){
            return null;
        }

        KeywordCall step = new KeywordCall();
        step.setKeyword(keyword);
        step.setFile(this.getFile());
        step.setName(keywordParameter.toString());

        int j = keyword.getMaxArgument() == -1 ? parameters.size() : keyword.getMaxArgument();
        for(int i = index + 1; i < parameters.size() && j > 0; ++i, --j){
            step.parameters.add(parameters.get(i));
        }

        return stepParameters.put(keywordParameter, step);
    }
}
