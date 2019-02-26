package org.ukwikora.model;

import org.apache.commons.lang3.StringUtils;
import org.ukwikora.analytics.Action;
import org.ukwikora.utils.LevenshteinDistance;


import javax.annotation.Nonnull;
import java.util.*;

public abstract class KeywordDefinition implements Keyword, Iterable<Step> {
    private TestCaseFile file;
    private Value name;
    private String documentation;
    private Set<String> tags;
    private List<Step> steps;
    private Set<Statement> dependencies;
    private LineRange lineRange;

    KeywordDefinition(){
        steps = new ArrayList<>();
        dependencies = new HashSet<>();
        tags = new HashSet<>();
        documentation = "";
    }

    public void setName(String name){
        this.name = new Value(name);
    }

    public void addStep(Step step){
        this.steps.add(step);
        step.addDependency(this);
    }

    public void addTag(String tag){
        if(!StringUtils.isBlank(tag)) {
            this.tags.add(tag);
        }
    }

    public void addDependency(KeywordDefinition keyword) {
        if(keyword != null) {
            this.dependencies.add(keyword);
        }
    }

    @Override
    public void setFile(@Nonnull TestCaseFile file) {
        this.file = file;

        for(Step step: this.steps){
            step.setFile(this.file);
        }
    }

    @Override
    public TestCaseFile getFile() {
        return file;
    }

    @Override
    public String getFileName() {
        if(this.file == null){
            return "";
        }

        return this.file.getName();
    }

    @Override
    public long getEpoch() {
        return file.getEpoch();
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

    @Override
    public Keyword getStep(int position) {
        if(steps.size() <= position  || 0 > position){
            return null;
        }

        return steps.get(position);
    }

    @Override
    public Set<Statement> getDependencies() {
        return dependencies;
    }

    @Override
    public void addDependency(@Nonnull Statement dependency) {
        this.dependencies.add(dependency);
    }


    public boolean matches(@Nonnull String name) {
        return this.name.matches(name);
    }

    @Nonnull
    public Iterator<Step> iterator() {
        return steps.iterator();
    }

    @Override
    public void execute(Runtime runtime) {

    }

    void getSequences(List<Sequence> sequences){
        for(Step step: steps){
            step.getSequences(sequences);
        }
    }

    @Override
    public double distance(@Nonnull Differentiable other) {
        if(other == this){
            return 0.0;
        }

        return (double)differences(other).size() / this.getLoc();
    }

    @Override
    public List<Action> differences(@Nonnull Differentiable other) {
        List<Action> actions = new ArrayList<>();

        if(other == this){
            return actions;
        }

        if(!KeywordDefinition.class.isAssignableFrom(other.getClass())){
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
    public Value.Type[] getArgumentTypes() {
        return new Value.Type[0];
    }

    @Override
    public int getMaxArgument() {
        return 0;
    }

    @Override
    public int[] getKeywordsLaunchedPosition() {
        return new int[0];
    }

    @Override
    public Type getType(){
        return Type.User;
    }

    @Override
    public void setLineRange(@Nonnull LineRange lineRange){
        this.lineRange = lineRange;
    }

    @Override
    public LineRange getLineRange(){
        return this.lineRange;
    }

    @Override
    public int getLoc() {
        return this.file.getLoc(this.lineRange);
    }
}
