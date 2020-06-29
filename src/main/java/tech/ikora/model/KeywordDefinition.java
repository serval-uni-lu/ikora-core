package tech.ikora.model;

import org.apache.commons.lang3.StringUtils;
import tech.ikora.analytics.Action;
import tech.ikora.builder.ValueResolver;
import tech.ikora.runner.Runtime;
import tech.ikora.utils.LevenshteinDistance;

import java.util.*;

public abstract class KeywordDefinition extends SourceNode implements Keyword, Iterable<Step>, Delayable, ScopeNode {
    private Token name;
    private Tokens documentation;
    private Set<Token> tags;
    private List<Step> steps;
    private TimeOut timeOut;

    protected List<Variable> localVariables;

    KeywordDefinition(Token name){
        steps = new ArrayList<>();
        tags = new HashSet<>();
        documentation = new Tokens();
        this.timeOut = TimeOut.none();
        localVariables = new ArrayList<>();

        this.name = name;
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
        this.addAstChild(step);
        step.addDependency(this);
        addTokens(step.getTokens());
    }

    public void addTag(Token tag){
        if(!StringUtils.isBlank(tag.getText())) {
            this.tags.add(tag);
        }
    }

    @Override
    public String toString() {
        return String.format("%s - %s", getLibraryName(), getNameToken());
    }

    public List<Step> getSteps() {
        return steps;
    }

    public void setDocumentation(Tokens documentation){
        this.documentation = documentation;
    }

    @Override
    public Token getNameToken() {
        return name;
    }

    @Override
    public Tokens getDocumentation() {
        return documentation;
    }

    public Set<Token> getTags() {
        return tags;
    }

    public Step getStep(int position) {
        if(steps.size() <= position  || 0 > position){
            return null;
        }

        return steps.get(position);
    }

    public boolean matches(Token token) {
        if(token == null){
            return false;
        }

        return ValueResolver.matches(this.name, token);
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
        return this.equals(other) ? 0. : 1.;
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
        if(!this.getNameToken().equalsIgnorePosition(keyword.getNameToken())){
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
        else if(!this.documentation.equalsIgnorePosition(keyword.documentation)){
            action = Action.changeDocumentation(this, keyword);
        }

        return action;
    }

    @Override
    public Type getType(){
        return Type.USER;
    }

    public List<Variable> getLocalVariables() {
        return new ArrayList<>(localVariables);
    }
}
