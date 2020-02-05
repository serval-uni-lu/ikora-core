package tech.ikora.model;

import org.apache.commons.lang3.StringUtils;
import tech.ikora.analytics.Action;
import tech.ikora.builder.ValueLinker;
import tech.ikora.runner.Runtime;
import tech.ikora.utils.LevenshteinDistance;

import java.util.*;

public abstract class KeywordDefinition extends Keyword implements Iterable<Step>, Delayable {
    private Token name;
    private String documentation;
    private Set<Token> tags;
    private List<Step> steps;
    private TimeOut timeOut;

    KeywordDefinition(){
        steps = new ArrayList<>();
        tags = new HashSet<>();
        documentation = "";
        this.timeOut = TimeOut.none();
    }

    public void setName(Token name){
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
        step.addDependency(this);
        step.setSourceFile(getSourceFile());
        addTokens(step.getTokens());
    }

    public void addTag(Token tag){
        if(!StringUtils.isBlank(tag.getText())) {
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
    public Token getName() {
        return name;
    }

    public String getDocumentation() {
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

        return ValueLinker.matches(this.name, token);
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
        if(other == this){
            return 0.0;
        }

        if(other == null || !this.getClass().isAssignableFrom(other.getClass())){
            return 1.0;
        }

        KeywordDefinition keyword = (KeywordDefinition)other;

        boolean sameName = this.getName().equalsValue(keyword.getName());
        boolean sameArguments = LevenshteinDistance.index(this.steps, keyword.steps) == 0.0;

        return sameName && sameArguments ? 0.0 : 1.0;
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
        if(!this.getName().equalsValue(keyword.getName())){
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
}
