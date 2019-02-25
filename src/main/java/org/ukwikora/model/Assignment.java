package org.ukwikora.model;

import org.ukwikora.analytics.Action;
import org.ukwikora.analytics.VisitorMemory;
import org.ukwikora.utils.LevenshteinDistance;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class Assignment extends Step {
    private List<Variable> returnValues;
    private KeywordCall expression;

    public Assignment(){
        returnValues = new ArrayList<>();
    }

    public KeywordCall getExpression() {
        return expression;
    }

    public List<Variable> getReturnValues() {
        return returnValues;
    }

    public void addReturnValue(@Nonnull Variable variable){
        returnValues.add(variable);
    }

    public void setExpression(@Nonnull KeywordCall call) {
        expression = call;
    }

    @Override
    public void setFile(@Nonnull TestCaseFile file){
        super.setFile(file);

        for(Variable variable: returnValues){
            variable.setFile(this.getFile());
        }
    }

    @Override
    public void setLineRange(@Nonnull LineRange lineRange){
        super.setLineRange(lineRange);

        for(Variable variable: returnValues){
            variable.setLineRange(this.getLineRange());
        }
    }

    @Override
    public boolean hasKeywordParameters() {
        if(getExpression() != null){
            return getExpression().hasKeywordParameters();
        }

        return false;
    }

    @Override
    public List<KeywordCall> getKeywordParameter() {
        if(getExpression() != null){
            return getExpression().getKeywordParameter();
        }

        return Collections.emptyList();
    }

    @Override
    public Keyword getStep(int position) {
        return null;
    }

    @Override
    public List<Value> getParameters() {
        if(expression == null){
            return new ArrayList<>();
        }

        return expression.getParameters();
    }

    @Override
    public Optional<Value> getParameter(int position, boolean resolved) {
        if(expression == null){
            return Optional.empty();
        }

        return expression.getParameter(position, resolved);
    }

    @Override
    public boolean hasParameters() {
        if(expression == null){
            return false;
        }

        return expression.hasParameters();
    }

    @Override
    public void execute(Runtime runtime) {

    }

    @Override
    public void getSequences(List<Sequence> sequences) {
        getExpression().getSequences(sequences);
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof Assignment)){
            return false;
        }

        if(!super.equals(other)) {
            return false;
        }

        Assignment assignment = (Assignment)other;

        boolean same = this.expression.equals(assignment.expression);

        for(int i = 0; same && i < this.returnValues.size(); ++i) {
            same &= this.returnValues.get(i).getName().equalsIgnoreCase(assignment.returnValues.get(i).getName());
        }

        return  same;
    }

    @Override
    public double distance(@Nonnull Differentiable other) {
        if(other == this){
            return 0.0;
        }

        if(!(other instanceof Assignment)){
            return 1;
        }

        Assignment assignment = (Assignment)other;

        double expressionIndex = expression.distance(assignment.expression);
        double returnValuesIndex = LevenshteinDistance.index(returnValues, assignment.returnValues);

        return (0.5 * expressionIndex) + (0.5 * returnValuesIndex);
    }

    @Override
    public List<Action> differences(@Nonnull Differentiable other) {
        List<Action> actions = new ArrayList<>();

        if(!(other instanceof Step)){
            return actions;
        }

        if(this.getClass() != other.getClass()){
            actions.add(Action.changeStepType(this, other));
        }
        else{
            Assignment assignment = (Assignment)other;

            if(this.getExpression().distance(assignment.getExpression()) > 0){
                actions.add(Action.changeStepExpression(this, assignment));
            }

            if(LevenshteinDistance.index(this.getReturnValues(), assignment.getReturnValues()) > 0){
                actions.add(Action.changeStepReturnValues(this, assignment));
            }
        }

        return actions;
    }

    @Override
    public String toString(){
        StringBuilder builder = new StringBuilder();

        for (Variable variable: returnValues){
            builder.append(variable.getName());
            builder.append("\t");
        }

        builder.append("\t=\t");
        builder.append(getExpression().toString());

        return builder.toString();
    }

    @Override
    public Value.Type[] getArgumentTypes() {
        if(this.expression == null){
            return new Value.Type[0];
        }

        return this.expression.getArgumentTypes();
    }

    @Override
    public int[] getKeywordsLaunchedPosition() {
        if(this.expression == null){
            return new int[0];
        }

        return this.expression.getKeywordsLaunchedPosition();
    }

    @Override
    public Type getType(){
        return this.expression.getType();
    }

    @Override
    public void accept(StatementVisitor visitor, VisitorMemory memory){
        visitor.visit(this, memory);
    }
}
