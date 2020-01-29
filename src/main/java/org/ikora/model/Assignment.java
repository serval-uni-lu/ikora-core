package org.ikora.model;

import org.ikora.analytics.Action;
import org.ikora.analytics.visitor.NodeVisitor;
import org.ikora.analytics.visitor.VisitorMemory;
import org.ikora.builder.Linker;
import org.ikora.exception.ExecutionException;
import org.ikora.exception.InvalidDependencyException;
import org.ikora.runner.Runtime;
import org.ikora.utils.LevenshteinDistance;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Assignment extends Step {
    private List<Variable> returnVariables;
    private KeywordCall expression;

    public Assignment(){
        super(Token.empty());
        returnVariables = new ArrayList<>();
    }

    public List<Variable> getReturnVariables() {
        return returnVariables;
    }

    public void addReturnValue(Variable variable) throws InvalidDependencyException {
        variable.addDependency(this);
        variable.setSourceFile(getSourceFile());

        returnVariables.add(variable);
    }

    public void setExpression(KeywordCall call) throws InvalidDependencyException {
        expression = call;
        expression.addDependency(this);
        expression.setSourceFile(getSourceFile());
    }

    @Override
    public void setSourceFile(SourceFile sourceFile){
        super.setSourceFile(sourceFile);

        for(Variable variable: returnVariables){
            variable.setSourceFile(this.getSourceFile());
        }

        if(expression != null){
            expression.setSourceFile(this.getSourceFile());
        }
    }

    @Override
    public List<Argument> getArgumentList() {
        if(expression == null){
            return new ArrayList<>();
        }

        return expression.getArgumentList();
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
        runtime.enterNode(this);

        if(expression != null){
            Linker.link(expression, runtime);

            if(!runtime.getErrors().isEmpty()){
                throw new ExecutionException(runtime.getErrors());
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

        runtime.exitNode(this);

        for(Variable variable: returnVariables){
            runtime.addToKeywordScope(this.getCaller(), variable);
        }
    }

    @Override
    public double distance(Differentiable other) {
        if(other == this){
            return 0.0;
        }

        if(!(other instanceof Assignment)){
            return 1;
        }

        Assignment assignment = (Assignment)other;

        return expression.distance(assignment.expression);
    }

    @Override
    public List<Action> differences(Differentiable other) {
        List<Action> actions = new ArrayList<>();

        if(other == null){
            actions.add(Action.invalid(this, other));
            return actions;
        }

        if(!(other instanceof Step)){
            return actions;
        }

        if(this.getClass() != other.getClass()){
            actions.add(Action.changeStepType(this, other));
        }
        else{
            Assignment assignment = (Assignment)other;

            getKeywordCall().ifPresent(call -> assignment.getKeywordCall().ifPresent(otherCall -> {

                if(call.distance(otherCall) > 0){
                    actions.add(Action.changeStepExpression(this, assignment));
                }

                if(LevenshteinDistance.index(this.getReturnVariables(), assignment.getReturnVariables()) > 0){
                    actions.add(Action.changeStepReturnValues(this, assignment));
                }
            }));

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
    public void accept(NodeVisitor visitor, VisitorMemory memory){
        visitor.visit(this, memory);
    }

    @Override
    public Optional<KeywordCall> getKeywordCall() {
        return Optional.ofNullable(expression);
    }

    private void assignVariables(List<Value> returnValues){
        //TODO: implement assignment
    }
}
