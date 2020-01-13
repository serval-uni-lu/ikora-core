package org.ikora.model;

import org.apache.commons.lang3.StringUtils;
import org.ikora.analytics.Action;
import org.ikora.runner.Runtime;
import org.ikora.utils.LevenshteinDistance;

import java.util.*;

public abstract class KeywordDefinition extends Keyword implements Iterable<Step>, Delayable {
    private Value name;
    private String documentation;
    private Set<String> tags;
    private List<Step> steps;
    private TimeOut timeOut;

    KeywordDefinition(){
        steps = new ArrayList<>();
        tags = new HashSet<>();
        documentation = "";
        this.timeOut = new TimeOut("NONE");
    }

    public void setName(String name){
        this.name = new Value(name);
    }

    @Override
    public TimeOut getTimeOut() {
        return timeOut;
    }

    @Override
    public void setTimeOut(TimeOut timeOut){
        this.timeOut = timeOut;
    }

    public void addStep(Step step) throws Exception {
        this.steps.add(step);
        step.addDependency(this);
        step.setSourceFile(getSourceFile());
    }

    public void addTag(String tag){
        if(!StringUtils.isBlank(tag)) {
            this.tags.add(tag);
        }
    }

    @Override
    public String toString() {
        return String.format("%s - %s", getFileName(), getName());
    }

    @Override
    public void setSourceFile(SourceFile sourceFile) {
        super.setSourceFile(sourceFile);

        for(Step step: this.steps){
            step.setSourceFile(getSourceFile());
        }
    }

    public List<Step> getSteps() {
        return steps;
    }

    public void setDocumentation(String documentation){
        this.documentation = documentation;
    }

    @Override
    public String getName() {
        return name.toString();
    }

    @Override
    public Value getNameAsValue() {
        return name;
    }

    public String getDocumentation() {
        return documentation;
    }


    public Set<String> getTags() {
        return tags;
    }

    public Step getStep(int position) {
        if(steps.size() <= position  || 0 > position){
            return null;
        }

        return steps.get(position);
    }


    public boolean matches(String name) {
        if(name == null){
            return false;
        }

        return this.name.matches(name);
    }

    public Iterator<Step> iterator() {
        return steps.iterator();
    }

    @Override
    public void execute(Runtime runtime) throws Exception{
        runtime.enterNode(this);

        for(Step step: this.steps){
            step.execute(runtime);
        }

        runtime.exitNode(this);
    }

    @Override
    public double distance(Differentiable other) {
        if(other == null){
            return 1.0;
        }

        if(other == this){
            return 0.0;
        }

        return (double)differences(other).size() / this.getLoc();
    }

    @Override
    public List<Action> differences(Differentiable other) {
        List<Action> actions = new ArrayList<>();

        if(other == this){
            return actions;
        }

        if(other == null || !KeywordDefinition.class.isAssignableFrom(other.getClass())){
            actions.add(Action.invalid(this, other));
            return actions;
        }

        KeywordDefinition keyword = (KeywordDefinition)other;

        // check name change
        if(!this.getName().equalsIgnoreCase(keyword.getName())){
            actions.add(Action.changeName(this, other));
        }

        // check documentation change
        Action documentationAction = differenceDocumentation(keyword);
        if(documentationAction != null){
            actions.add(documentationAction);
        }

        // check step changes
        List<Action> stepActions = LevenshteinDistance.getDifferences(this.getSteps(), keyword.getSteps());
        actions.addAll(stepActions);

        return actions;
    }

    private Action differenceDocumentation(KeywordDefinition keyword) {
        Action action = null;

        if(this.documentation.isEmpty() && !keyword.documentation.isEmpty()){
            action = Action.addDocumentation(this, keyword);
        }
        else if(!this.documentation.isEmpty() && keyword.documentation.isEmpty()){
            action = Action.removeDocumentation(this, keyword);
        }
        else if(!this.documentation.equals(keyword.documentation)){
            action = Action.changeDocumentation(this, keyword);
        }

        return action;
    }

    @Override
    public Type getType(){
        return Type.USER;
    }

    KeywordCall toCall(Step step){
        if(step == null){
            return null;
        }

        if(KeywordCall.class.isAssignableFrom(step.getClass())){
            return (KeywordCall) step;
        }

        return null;
    }
}
