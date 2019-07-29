package org.ukwikora.model;

import org.ukwikora.analytics.Action;
import org.ukwikora.analytics.VisitorMemory;
import org.ukwikora.builder.Linker;
import org.ukwikora.exception.InvalidDependencyException;
import org.ukwikora.exception.InvalidImportTypeException;
import org.ukwikora.runner.Runtime;
import org.ukwikora.utils.LevenshteinDistance;

import javax.annotation.Nonnull;
import java.util.*;

public class KeywordCall extends Step {
    private Link<KeywordCall, Keyword> link;
    private List<Value> parameters;
    private List<Value> returnValues;
    private Map<Value, KeywordCall> stepParameters;

    public KeywordCall() {
        this.parameters = new ArrayList<>();
        this.stepParameters = new HashMap<>();
        this.link = new Link<>(this);
    }

    public void linkKeyword(Keyword keyword, Link.Import importLink)
            throws InvalidImportTypeException, InvalidDependencyException {
        link.addStatement(keyword, importLink);
    }

    public void addParameter(String value) {
        this.parameters.add(new Value(this, value));
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

    public Optional<Keyword> getKeyword() {
        return link.getStatement();
    }

    public Set<Keyword> getAllPotentialKeywords(Link.Import importType){
        return link.getAllLinks(importType);
    }

    @Override
    public Keyword getStep(int position) {
        if(!getKeyword().isPresent()) {
            return null;
        }

        return getKeyword().get().getStep(position);
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
    public void execute(Runtime runtime) throws Exception{
        runtime.enterKeyword(this);

        Linker.link(this, runtime);

        Optional<Keyword> callee = link.getStatement();

        if(callee.isPresent()){
            callee.get().execute(runtime);
            returnValues = runtime.getReturnValues();
        }
        else{
            throw new Exception("Need to have a better exception");
        }

        runtime.exitKeyword(this);
    }

    @Override
    public Value.Type[] getArgumentTypes() {
        if(!getKeyword().isPresent()){
            return new Value.Type[0];
        }

        return getKeyword().get().getArgumentTypes();
    }

    @Override
    public int[] getKeywordsLaunchedPosition() {
        if(!getKeyword().isPresent()){
            return new int[0];
        }

        int[] positions = getKeyword().get().getKeywordsLaunchedPosition();

        if(positions.length == 1 && positions[0] == -1){
            positions = new int[getParameters().size()];
            for (int i = 0; i < positions.length; ++i) {
                positions[i] = i;
            }
        }

        return positions;
    }

    @Override
    public double distance(@Nonnull Differentiable other) {
        if(!(other instanceof KeywordCall)){
            return 1;
        }

        KeywordCall call = (KeywordCall)other;

        double nameIndex = LevenshteinDistance.stringIndex(getName(), call.getName());
        double parameterIndex = LevenshteinDistance.index(getParameters(), call.getParameters());

        return (0.5 * nameIndex) + (0.5 * parameterIndex);
    }

    @Override
    public List<Action> differences(@Nonnull  Differentiable other) {
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

        KeywordCall step;

        try {
            step = new KeywordCall();
            step.addDependency(this);
            step.linkKeyword(keyword, Link.Import.STATIC);
            step.setFile(this.getFile());
            step.setName(keywordParameter.toString());

            int j = keyword.getMaxArgument() == -1 ? parameters.size() : keyword.getMaxArgument();
            for(int i = index + 1; i < parameters.size() && j > 0; ++i, --j){
                step.parameters.add(parameters.get(i));
            }

            stepParameters.put(keywordParameter, step);
        } catch (Exception e) {
            step = null;
        }

        return step;
    }

    @Override
    public Type getType(){
        if(!getKeyword().isPresent()){
            return Type.Unknown;
        }

        return getKeyword().get().getType();
    }

    @Override
    public List<Value> getReturnValues() {
        return this.returnValues;
    }

    @Override
    public boolean hasKeywordParameters() {
        return !stepParameters.isEmpty();
    }

    @Override
    public List<KeywordCall> getKeywordParameter() {
        return new ArrayList<>(stepParameters.values());
    }

    @Override
    public void accept(StatementVisitor visitor, VisitorMemory memory){
        visitor.visit(this, memory);
    }

    @Override
    public Optional<KeywordCall> getKeywordCall() {
        return Optional.of(this);
    }
}
