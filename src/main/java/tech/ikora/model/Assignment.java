package tech.ikora.model;

import tech.ikora.analytics.Action;
import tech.ikora.analytics.visitor.NodeVisitor;
import tech.ikora.analytics.visitor.VisitorMemory;
import tech.ikora.builder.SymbolResolver;
import tech.ikora.exception.ExecutionException;
import tech.ikora.runner.Runtime;
import tech.ikora.utils.LevenshteinDistance;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class Assignment extends Step {
    private List<Variable> leftHandOperand;
    private Argument rightHandOperand;

    public Assignment(Token name, List<Variable> leftHandOperand, KeywordCall rightHandOperand) {
        super(name);

        this.leftHandOperand = new ArrayList<>(leftHandOperand.size());

        for(Variable returnVariable: leftHandOperand){
            this.addReturnVariable(returnVariable);
            this.addAstChild(returnVariable);
            this.addTokens(returnVariable.getTokens());
        }

        this.rightHandOperand = new Argument(rightHandOperand);
        this.addAstChild(rightHandOperand);
        this.addTokens(this.rightHandOperand.getTokens());
    }

    public void addReturnVariable(Variable variable)  {
        if(variable == null){
            return;
        }

        this.addAstChild(variable);
        leftHandOperand.add(variable);
    }

    public List<Variable> getLeftHandOperand() {
        return leftHandOperand;
    }

    @Override
    public ArgumentList getArgumentList() {
        return getKeywordCall().map(KeywordCall::getArgumentList).orElse(new ArgumentList());
    }

    @Override
    public boolean hasParameters() {
        return getKeywordCall().map(KeywordCall::hasParameters).orElse(false);
    }

    @Override
    public Optional<KeywordCall> getKeywordCall(){
        if(rightHandOperand == null){
            return Optional.empty();
        }

        SourceNode sourceNode = rightHandOperand.getDefinition().orElse(null);

        if(sourceNode instanceof KeywordCall){
            return Optional.of((KeywordCall) sourceNode);
        }

        return Optional.empty();
    }

    @Override
    public void execute(Runtime runtime) throws Exception {
        runtime.enterNode(this);

        Optional<KeywordCall> optional = getKeywordCall();

        if(optional.isPresent()){
            KeywordCall call = optional.get();
            SymbolResolver.resolve(call, runtime);

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

        for(Variable variable: leftHandOperand){
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

        double distReturn = LevenshteinDistance.index(this.leftHandOperand, assignment.leftHandOperand) * 0.2;
        double distExpression = rightHandOperand.distance(assignment.rightHandOperand) * 0.8;

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
            actions.addAll(LevenshteinDistance.getDifferences(this.leftHandOperand, assignment.leftHandOperand));
            getKeywordCall().ifPresent(call -> actions.addAll(call.differences(assignment.getKeywordCall().orElse(null))));
        }
        else if(other instanceof KeywordCall){
            actions.addAll(LevenshteinDistance.getDifferences(this.leftHandOperand, Collections.emptyList()));
            getKeywordCall().ifPresent(call -> actions.addAll(call.differences(other)));
        }
        else {
            actions.add(Action.changeType(this, other));
        }

        return actions;
    }

    @Override
    public String toString(){
        StringBuilder builder = new StringBuilder();

        for (Variable variable: leftHandOperand){
            builder.append(variable.getNameToken());
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
