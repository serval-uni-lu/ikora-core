package lu.uni.serval.robotframework.model;

import lu.uni.serval.analytics.Action;
import lu.uni.serval.robotframework.runner.Runtime;
import lu.uni.serval.utils.Differentiable;
import lu.uni.serval.utils.LevenshteinDistance;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KeywordCall extends Step {
    private Keyword keyword;
    private List<Argument> parameters;
    private Map<Argument, Keyword> keywordParameters;

    public KeywordCall() {
        this.parameters = new ArrayList<>();
        this.keywordParameters = new HashMap<>();
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
        else if(keywordParameters.size() > 0){
            int size = 0;

            for (Keyword keywordLaunched: keywordParameters.values()){
                if(keywordLaunched == null){
                    continue;
                }

                size += keywordLaunched.getSize();
            }

            return size;
        }

        return this.keyword.getSize();
    }

    @Override
    public List<Keyword> getSequence() {
        if(this.keyword == null){
            return new ArrayList<>();
        }

        return this.keyword.getSequence();
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

    private boolean hasKeywordsArgument(){
        if(this.keyword == null){
            return false;
        }

        for(Argument.Type type: this.keyword.getArgumentTypes()){
            if(type == Argument.Type.Keyword){
                return true;
            }
        }

        return false;
    }

    public void setKeywordParameter(Argument keywordParameter, Keyword keyword) {
        keywordParameters.put(keywordParameter, keyword);
    }
}
