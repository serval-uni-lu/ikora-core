package org.ikora.model;

import org.ikora.analytics.Action;
import org.ikora.analytics.visitor.NodeVisitor;
import org.ikora.analytics.visitor.VisitorMemory;
import org.ikora.exception.InvalidDependencyException;
import org.ikora.runner.Runtime;
import org.ikora.utils.LevenshteinDistance;
import org.apache.commons.lang3.NotImplementedException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class ForLoop extends Step {
    private final List<Step> steps;
    private final Variable iterator;
    private final Step range;

    public ForLoop(Variable iterator, Step range, List<Step> steps) {
        super(Token.empty());

        this.iterator = iterator;
        this.range = range;
        this.steps = steps;
    }

    public List<Step> getSteps(){
        return steps;
    }

    public Variable getIterator() {
        return iterator;
    }

    public Step getRange() {
        return range;
    }

    @Override
    public List<Argument> getArgumentList() {
        return Collections.emptyList();
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
    public double distance(Differentiable other) {
        //TODO: implement a proper distance method
        return 0.0;
    }

    @Override
    public List<Action> differences(Differentiable other) {
        //TODO: implement the a proper differences method
        return Collections.emptyList();
    }

    @Override
    public void accept(NodeVisitor visitor, VisitorMemory memory){
        visitor.visit(this, memory);
    }

    @Override
    public void setTemplate(KeywordCall template) throws InvalidDependencyException {
        super.setTemplate(template);

        for(Step step: steps){
            step.setTemplate(template);
        }
    }
}
