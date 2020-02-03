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
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class Assignment extends Step {
    private List<Variable> returnVariables;
    private Argument expression;

    public Assignment(Token name, List<Variable> returnVariables, KeywordCall expression) throws InvalidDependencyException {
        super(name);

        this.returnVariables = new ArrayList<>(returnVariables.size());
        for(Variable returnVariable: returnVariables){
            this.addReturnVariable(returnVariable);
        }

        this.expression = new Argument(this, expression);
    }

    public void addReturnVariable(Variable variable) throws InvalidDependencyException {
        variable.addDependency(this);
        variable.setSourceFile(getSourceFile());
        variable.addArgument(this.expression);

        returnVariables.add(variable);
    }

    public List<Variable> getReturnVariables() {
        return returnVariables;
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
        return getKeywordCall().map(KeywordCall::getArgumentList).orElse(Collections.emptyList());
    }

    @Override
    public boolean hasParameters() {
        return getKeywordCall().map(KeywordCall::hasParameters).orElse(false);
    }

    @Override
    public Optional<KeywordCall> getKeywordCall(){
        if(expression == null){
            return Optional.empty();
        }

        Node node = expression.getDefinition().orElse(null);

        if(node instanceof KeywordCall){
            return Optional.of((KeywordCall)node);
        }

        return Optional.empty();
    }

    @Override
    public void execute(Runtime runtime) throws Exception {
        runtime.enterNode(this);

        Optional<KeywordCall> optional = getKeywordCall();

        if(optional.isPresent()){
            KeywordCall call = optional.get();
            Linker.link(call, runtime);

            if(!runtime.getErrors().isEmpty()){
                throw new ExecutionException(runtime.getErrors());
            }

            Optional<Keyword> callee = call.getKeyword();

            if(callee.isPresent()){
                callee.get().execute(runtime);
                assignVariables(runtime.getReturnVariables());
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

        boolean sameReturnValues = LevenshteinDistance.index(this.returnVariables, assignment.returnVariables) == 0.0;
        boolean sameExpression = expression.distance(assignment.expression) == 0.0;

        return sameReturnValues && sameExpression ? 0.0 : 1.0;
    }

    @Override
    public List<Action> differences(Differentiable other) {
        if(other == null){
            return Collections.singletonList(Action.addElement(this.getClass(), this));
        }

        if(!(other instanceof Step)){
            return Collections.emptyList();
        }

        List<Action> actions = new ArrayList<>();

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

    private void assignVariables(List<Variable> returnValues){
        //TODO: implement assignment
    }
}
