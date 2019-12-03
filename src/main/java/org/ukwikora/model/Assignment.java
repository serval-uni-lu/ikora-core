package org.ukwikora.model;

import org.ukwikora.analytics.Action;
import org.ukwikora.analytics.NodeVisitor;
import org.ukwikora.analytics.VisitorMemory;
import org.ukwikora.builder.Linker;
import org.ukwikora.error.Error;
import org.ukwikora.exception.ExecutionException;
import org.ukwikora.exception.InvalidDependencyException;
import org.ukwikora.runner.Runtime;
import org.ukwikora.utils.LevenshteinDistance;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class Assignment extends Step {
    private List<Variable> returnVariables;
    private KeywordCall expression;

    public Assignment(){
        returnVariables = new ArrayList<>();
    }

    public List<Variable> getReturnVariables() {
        return returnVariables;
    }

    public void addReturnValue(@Nonnull Variable variable){
        returnVariables.add(variable);
    }

    public void setExpression(@Nonnull KeywordCall call) throws InvalidDependencyException {
        expression = call;
        expression.addDependency(this);
    }

    @Override
    public void setFile(@Nonnull SourceFile file){
        super.setFile(file);

        for(Variable variable: returnVariables){
            variable.setFile(this.getFile());
        }

        expression.setFile(this.getFile());
    }

    @Override
    public void setLineRange(@Nonnull LineRange lineRange){
        super.setLineRange(lineRange);

        for(Variable variable: returnVariables){
            variable.setLineRange(this.getLineRange());
        }

        expression.setLineRange(this.getLineRange());
    }

    @Override
    public boolean hasKeywordParameters() {
        return getKeywordCall().map(KeywordCall::hasKeywordParameters).orElse(false);

    }

    @Override
    public List<KeywordCall> getKeywordParameter() {
        Optional<KeywordCall> call = getKeywordCall();

        if(call.isPresent()){
            return call.get().getKeywordParameter();
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
    public void execute(Runtime runtime) throws Exception {
        runtime.enterKeyword(this);

        if(expression != null){
            List<Error> errors = new ArrayList<>();
            Linker.link(expression, runtime, errors);

            if(!errors.isEmpty()){
                throw new ExecutionException(errors);
            }

            Optional<Keyword> callee = expression.getKeyword();

            if(callee.isPresent()){
                callee.get().execute(runtime);
                assignVariables(runtime.getReturnValues());
            }
            else{
                throw new Exception("Need to have a better exception");
            }
        }

        runtime.exitKeyword(this);

        for(Variable variable: returnVariables){
            runtime.addToKeywordScope(this.getParent(), variable);
        }
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

        for(int i = 0; same && i < this.returnVariables.size(); ++i) {
            same &= this.returnVariables.get(i).getName().equalsIgnoreCase(assignment.returnVariables.get(i).getName());
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
        double returnValuesIndex = LevenshteinDistance.index(returnVariables, assignment.returnVariables);

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

            getKeywordCall().ifPresent(call -> {
                assignment.getKeywordCall().ifPresent(otherCall -> {

                    if(call.distance(otherCall) > 0){
                        actions.add(Action.changeStepExpression(this, assignment));
                    }

                    if(LevenshteinDistance.index(this.getReturnVariables(), assignment.getReturnVariables()) > 0){
                        actions.add(Action.changeStepReturnValues(this, assignment));
                    }
                });
            });

        }

        return actions;
    }

    @Override
    public String toString(){
        StringBuilder builder = new StringBuilder();

        for (Variable variable: returnVariables){
            builder.append(variable.getName());
            builder.append("\t");
        }

        builder.append("\t=\t");

        getKeywordCall().ifPresent(call -> builder.append(call.toString()));

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
    public List<Value> getReturnValues() {
        return Collections.emptyList();
    }

    @Override
    public void accept(NodeVisitor visitor, VisitorMemory memory){
        visitor.visit(this, memory);
    }

    @Override
    public Optional<KeywordCall> getKeywordCall() {
        return Optional.ofNullable(expression);
    }

    private void assignVariables(List<Value> returnValues){

    }
}
