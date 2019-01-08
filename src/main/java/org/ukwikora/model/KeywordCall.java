package org.ukwikora.model;

import org.ukwikora.analytics.Action;
import org.ukwikora.utils.LevenshteinDistance;

import java.util.*;

public class KeywordCall extends Step {
    private Keyword keyword;
    private List<Value> parameters;
    private Map<Value, KeywordCall> stepParameters;

    public KeywordCall() {
        this.parameters = new ArrayList<>();
        this.stepParameters = new HashMap<>();
    }

    public void setKeyword(Keyword keyword){
        this.keyword = keyword;

        if(this.keyword != null && getParent() != null) {
            this.keyword.addDependency(getParent());
        }
    }

    public void addParameter(String value) {
        this.parameters.add(new Value(value));
    }

    public List<Value> getParameters() {
        return this.parameters;
    }

    @Override
    public Optional<Value> getParameter(int position, boolean resolved) {
        if (position < 0){
            return Optional.empty();
        }

        if(resolved){
            int current = 0;
            for(Value parameter: this.parameters){
                Optional<List<Value>> resolvedParameters  = parameter.getResolvedValues();

                if(!resolvedParameters.isPresent()){
                    return Optional.empty();
                }

                for(Value resolvedParameter: resolvedParameters.get()){
                    if(position == current){
                        return Optional.of(resolvedParameter);
                    }

                    ++current;
                }
            }
        }

        if(position < this.parameters.size()){
            return Optional.ofNullable(this.parameters.get(position));
        }

        return Optional.empty();
    }

    @Override
    public boolean hasParameters() {
        return !this.parameters.isEmpty();
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
    public int getLevel() {
        if(this.keyword == null){
            return 0;
        }
        else if(stepParameters.size() > 0){
            int depth = 0;

            for (Step step: stepParameters.values()){
                if(step == null){
                    continue;
                }

                depth = Math.max(step.getLevel(), depth);
            }

            return depth;
        }

        return this.keyword.getLevel();
    }

    @Override
    public void getSequences(List<Sequence> sequences) {
        if(this.keyword == null){
            return;
        }

        if(this.keyword instanceof LibraryKeyword){
            getLibrarySequence((LibraryKeyword)this.keyword, sequences);
        }
        else if(this.keyword instanceof KeywordDefinition){
            ((KeywordDefinition)keyword).getSequences(sequences);
        }
    }

    private void getLibrarySequence(LibraryKeyword keyword, List<Sequence> sequences) {
        switch (keyword.getType()){
            case Action:
            case Assertion:
            case Synchronisation:
            case ControlFlow:
            {
                for(Sequence sequence: sequences){
                    sequence.addStep(this);
                }
            }
            break;
/*
            case ControlFlow:
            {
                //TODO: properly handle case where there is no forking

                List<List<Keyword>> alternates = new ArrayList<>();

                for(List<Keyword> sequence: sequences) {
                    alternates.add(new ArrayList<>(sequence));
                }

                for(KeywordCall call: stepParameters.values()){
                    call.getTimeLines(alternates);
                }

                sequences.addAll(alternates);
            }
            break;
*/
        }
    }

    @Override
    public Value.Type[] getArgumentTypes() {
        if(this.keyword == null){
            return new Value.Type[0];
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
    public double distance(Differentiable other) {
        if(!(other instanceof KeywordCall)){
            return 1;
        }

        KeywordCall call = (KeywordCall)other;

        double nameIndex = LevenshteinDistance.stringIndex(getName(), call.getName());
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

            if(!this.getName().equalsIgnoreCase(call.getName())){
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

        builder.append(getName());

        for (Value parameter: parameters){
            builder.append("\t");
            builder.append(parameter.toString());
        }

        return builder.toString();
    }

    public KeywordCall setKeywordParameter(Value keywordParameter, Keyword keyword) {
        int index = parameters.indexOf(keywordParameter);

        if(index < 0){
            return null;
        }

        KeywordCall step = new KeywordCall();
        step.setParent(this.getParent());
        step.setKeyword(keyword);
        step.setFile(this.getFile());
        step.setName(keywordParameter.toString());

        int j = keyword.getMaxArgument() == -1 ? parameters.size() : keyword.getMaxArgument();
        for(int i = index + 1; i < parameters.size() && j > 0; ++i, --j){
            step.parameters.add(parameters.get(i));
        }

        stepParameters.put(keywordParameter, step);

        return step;
    }

    @Override
    public Type getType(){
        if(this.keyword == null){
            return Type.Unknown;
        }

        return this.keyword.getType();
    }
}
