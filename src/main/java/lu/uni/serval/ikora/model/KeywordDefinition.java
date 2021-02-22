package lu.uni.serval.ikora.model;

import lu.uni.serval.ikora.analytics.difference.Edit;
import lu.uni.serval.ikora.builder.ValueResolver;
import lu.uni.serval.ikora.runner.Runtime;
import lu.uni.serval.ikora.utils.Ast;
import lu.uni.serval.ikora.utils.LevenshteinDistance;

import java.util.*;
import java.util.stream.Collectors;

public abstract class KeywordDefinition extends SourceNode implements Keyword, Iterable<Step>, Delayable, ScopeNode {
    private final Token name;
    private final NodeList<Step> steps;
    private final Set<SourceNode> dependencies;
    private Tokens documentation;
    private NodeList<Literal> tags;
    private TimeOut timeOut;

    private final List<Variable> localVariables;

    KeywordDefinition(Token name){
        this.dependencies = new HashSet<>();

        this.steps = new NodeList<>();
        this.addAstChild(this.steps);

        this.tags = new NodeList<>();
        this.addAstChild(this.tags);

        this.timeOut = TimeOut.none();
        this.addAstChild(this.timeOut);

        this.documentation = new Tokens();
        this.localVariables = new ArrayList<>();

        this.name = name;
    }

    public void addStep(Step step) throws Exception {
        this.steps.add(step);
        addTokens(step.getTokens());
    }

    public void setTags(NodeList<Literal> tags) {
        this.removeAstChild(this.tags);
        this.tags = tags;
        this.addAstChild(this.tags);

        addTokens(this.tags.getTokens());
    }

    public void addLocalVariables(NodeList<Variable> variables){
        localVariables.addAll(variables);
    }

    public void addLocalVariable(Variable variable){
        localVariables.add(variable);
    }

    public NodeList<Literal> getTags() {
        return tags;
    }

    public Step getStep(int position) {
        if(steps.size() <= position  || 0 > position){
            return null;
        }

        return steps.get(position);
    }

    public <T> Set<T> getUsages(Class<T> type){
        return getDependencies().stream()
                .map(d -> Ast.getParentByType(d, type))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toSet());
    }

    @Override
    public TimeOut getTimeOut() {
        return timeOut;
    }

    @Override
    public void setTimeOut(TimeOut timeOut){
        this.removeAstChild(this.timeOut);
        this.timeOut = timeOut;
        this.addAstChild(this.timeOut);
    }

    @Override
    public String toString() {
        return String.format("%s - %s", getLibraryName(), getNameToken());
    }

    public NodeList<Step> getSteps() {
        return steps;
    }

    public int getStepCount(){
        int count = 0;

        for(Step step: steps){
            count += step.getStepCount() + 1;
        }

        return count;
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

    @Override
    public boolean matches(Token token) {
        if(token == null){
            return false;
        }

        return ValueResolver.matches(this.name, token);
    }

    @Override
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
    public double distance(SourceNode other) {
        return this.equals(other) ? 0. : 1.;
    }

    @Override
    public List<Edit> differences(SourceNode other) {
        List<Edit> edits = new ArrayList<>();

        if(other == this){
            return edits;
        }

        if(other == null || !KeywordDefinition.class.isAssignableFrom(other.getClass())){
            edits.add(Edit.invalid(this, other));
            return edits;
        }

        KeywordDefinition keyword = (KeywordDefinition)other;

        // check name change
        if(!this.getNameToken().equalsIgnorePosition(keyword.getNameToken())){
            edits.add(Edit.changeName(this, other));
        }

        // check documentation change
        Edit documentationEdit = differenceDocumentation(keyword);
        if(documentationEdit != null){
            edits.add(documentationEdit);
        }

        // check step changes
        List<Edit> stepEdits = LevenshteinDistance.getDifferences(this.getSteps(), keyword.getSteps());
        edits.addAll(stepEdits);

        return edits;
    }

    @Override
    public Type getType(){
        return Type.USER;
    }

    @Override
    public List<Dependable> findDefinition(Variable variable) {
        return this.steps.stream()
                .filter(s -> s instanceof Assignment)
                .map(s -> (Assignment)s)
                .filter(a -> a.isDefinition(variable))
                .map(a -> (Dependable)a)
                .collect(Collectors.toList());
    }

    @Override
    public void addDependency(SourceNode node) {
        if(node == null) {
            return;
        }

        this.dependencies.add(node);
    }

    @Override
    public void removeDependency(SourceNode node) {
        this.dependencies.remove(node);
    }

    @Override
    public Set<SourceNode> getDependencies() {
        return dependencies;
    }

    private Edit differenceDocumentation(KeywordDefinition keyword) {
        Edit edit = null;

        if(this.documentation.isEmpty() && !keyword.documentation.isEmpty()){
            edit = Edit.addDocumentation(this, keyword);
        }
        else if(!this.documentation.isEmpty() && keyword.documentation.isEmpty()){
            edit = Edit.removeDocumentation(this, keyword);
        }
        else if(!this.documentation.equalsIgnorePosition(keyword.documentation)){
            edit = Edit.changeDocumentation(this, keyword);
        }

        return edit;
    }
}
