package tech.ikora.model;

import tech.ikora.analytics.Action;
import tech.ikora.analytics.visitor.NodeVisitor;
import tech.ikora.analytics.visitor.VisitorMemory;
import tech.ikora.builder.Linker;
import tech.ikora.exception.ExecutionException;
import tech.ikora.runner.Runtime;
import tech.ikora.utils.LevenshteinDistance;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class Assignment extends Step {
    private List<Variable> returnVariables;
    private Argument expression;

    public Assignment(Token name, List<Variable> returnVariables, KeywordCall expression) {
        super(name);

        this.returnVariables = new ArrayList<>(returnVariables.size());

        for(Variable returnVariable: returnVariables){
            this.addReturnVariable(returnVariable);
            this.addAstChild(returnVariable);
            this.addTokens(returnVariable.getTokens());
        }

        this.expression = new Argument(expression);
        this.addAstChild(expression);
        this.addTokens(this.expression.getTokens());
    }

    public void addReturnVariable(Variable variable)  {
        if(variable == null){
            return;
        }

        this.addAstChild(variable);
        variable.addArgument(this.expression);
        returnVariables.add(variable);
    }

    public List<Variable> getReturnVariables() {
        return returnVariables;
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
            return 0.;
        }

        if(!(other instanceof Assignment)){
            return 1.;
        }

        Assignment assignment = (Assignment)other;

        double distReturn = LevenshteinDistance.index(this.returnVariables, assignment.returnVariables) * 0.2;
        double distExpression = expression.distance(assignment.expression) * 0.8;

        return distReturn + distExpression;
    }

    @Override
    public List<Action> differences(Differentiable other) {
        if(other == null){
            return Collections.singletonList(Action.addElement(this.getClass(), this));
        }

        if(other == this){
            return Collections.emptyList();
        }

        List<Action> actions = new ArrayList<>();

        if(other instanceof Assignment){
            Assignment assignment = (Assignment)other;
            actions.addAll(LevenshteinDistance.getDifferences(this.returnVariables, assignment.returnVariables));
            getKeywordCall().ifPresent(call -> actions.addAll(call.differences(assignment.getKeywordCall().orElse(null))));
        }
        else if(other instanceof KeywordCall){
            actions.addAll(LevenshteinDistance.getDifferences(this.returnVariables, Collections.emptyList()));
            getKeywordCall().ifPresent(call -> actions.addAll(call.differences(other)));
        }
        else {
            actions.add(Action.changeStepType(this, other));
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
