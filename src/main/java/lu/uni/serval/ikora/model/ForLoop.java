package lu.uni.serval.ikora.model;

import lu.uni.serval.ikora.analytics.difference.Edit;
import lu.uni.serval.ikora.analytics.visitor.NodeVisitor;
import lu.uni.serval.ikora.analytics.visitor.VisitorMemory;
import lu.uni.serval.ikora.runner.Runtime;
import lu.uni.serval.ikora.utils.LevenshteinDistance;
import org.apache.commons.lang3.NotImplementedException;

import java.util.*;
import java.util.stream.Collectors;

public class ForLoop extends Step implements Dependable, ScopeNode {
    private final NodeList<Step> steps;
    private final Variable iterator;
    private final Step interval;
    private final Set<SourceNode> dependencies;

    protected List<Variable> localVariables;

    public ForLoop(Token name, Variable iterator, Step interval, List<Step> steps) {
        super(name);

        this.iterator = iterator;
        this.addAstChild(this.iterator);

        this.interval = interval;
        this.addAstChild(this.interval);

        this.addToken(name);
        this.addTokens(this.iterator.getTokens());
        this.addTokens(this.interval.getTokens());

        this.localVariables = new ArrayList<>();
        this.dependencies = new HashSet<>();


        this.steps = new NodeList<>(steps);
        this.addAstChild(this.steps);
    }

    public Variable getIterator() {
        return iterator;
    }

    public Step getInterval() {
        return interval;
    }

    @Override
    public List<Step> getSteps(){
        return this.steps;
    }

    @Override
    public NodeList<Argument> getArgumentList() {
        return new NodeList<>();
    }

    @Override
    public boolean hasParameters() {
        return false;
    }

    @Override
    public Optional<KeywordCall> getKeywordCall() {
        return Optional.empty();
    }

    @Override
    public void execute(Runtime runtime) {
        throw new NotImplementedException("Didn't implemented the execution module yet");
    }

    @Override
    public double distance(SourceNode other) {
        if(other == this){
            return 0.0;
        }

        if(other == null || !this.getClass().isAssignableFrom(other.getClass())){
            return 0.0;
        }

        ForLoop forLoop = (ForLoop)other;

        double sameIterator = this.iterator.distance(forLoop.iterator) * 0.1;
        double sameRange = this.interval.distance(forLoop.interval) * 0.1;
        double sameSteps = LevenshteinDistance.index(this.steps, forLoop.steps) * 0.8;

        return sameIterator + sameRange + sameSteps;
    }

    @Override
    public List<Edit> differences(SourceNode other) {
        if(other == null){
            return Collections.singletonList(Edit.removeElement(this.getClass(), this));
        }

        if(other == this){
            return Collections.emptyList();
        }

        List<Edit> edits = new ArrayList<>();

        if(!this.getClass().isAssignableFrom(other.getClass())){
            edits.add(Edit.changeStep(this, other));
            return edits;
        }

        ForLoop forLoop = (ForLoop)other;

        edits.addAll(this.iterator.differences(forLoop.iterator));
        edits.addAll(this.interval.differences(forLoop.interval));
        edits.addAll(LevenshteinDistance.getDifferences(this.steps, forLoop.steps));

        return edits;
    }

    @Override
    public void accept(NodeVisitor visitor, VisitorMemory memory){
        visitor.visit(this, memory);
    }

    @Override
    public void setTemplate(KeywordCall template) {
        super.setTemplate(template);

        for(Step step: steps){
            step.setTemplate(template);
        }
    }

    @Override
    public List<Dependable> findDefinition(Variable variable) {
        final List<Dependable> definitions = this.steps.stream()
                .filter(s -> s instanceof Assignment)
                .map(s -> (Assignment)s)
                .filter(a -> a.isDefinition(variable))
                .map(a -> (Dependable)a)
                .collect(Collectors.toList());

        if(iterator.matches(variable.getNameToken())){
            definitions.add(this);
        }


        return definitions;
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
}
