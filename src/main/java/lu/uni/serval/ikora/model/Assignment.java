package lu.uni.serval.ikora.model;

import lu.uni.serval.ikora.analytics.difference.Edit;
import lu.uni.serval.ikora.analytics.visitor.NodeVisitor;
import lu.uni.serval.ikora.analytics.visitor.VisitorMemory;
import lu.uni.serval.ikora.builder.SymbolResolver;
import lu.uni.serval.ikora.exception.ExecutionException;
import lu.uni.serval.ikora.runner.Runtime;
import lu.uni.serval.ikora.utils.LevenshteinDistance;

import java.util.*;

public class Assignment extends Step implements Dependable {
    private final NodeList<Variable> leftHandOperand;
    private final Argument rightHandOperand;
    private final Set<SourceNode> dependencies;

    public Assignment(Token name, List<Variable> leftHandOperand, KeywordCall rightHandOperand) {
        super(name);

        this.dependencies = new HashSet<>();

        this.leftHandOperand = new NodeList<>();
        this.addAstChild(this.leftHandOperand);

        for(Variable returnVariable: leftHandOperand){
            this.addReturnVariable(returnVariable);
        }

        if(rightHandOperand != null){
            this.rightHandOperand = new Argument(rightHandOperand);
            this.addAstChild(this.rightHandOperand);

            this.addTokens(this.rightHandOperand.getTokens());
        }
        else{
            this.rightHandOperand = null;
        }
    }

    public void addReturnVariable(Variable variable)  {
        if(variable == null){
            return;
        }

        this.addAstChild(variable);
        this.addTokens(variable.getTokens());

        leftHandOperand.add(variable);
    }

    public NodeList<Variable> getLeftHandOperand() {
        return leftHandOperand;
    }

    @Override
    public NodeList<Argument> getArgumentList() {
        return getKeywordCall().map(KeywordCall::getArgumentList).orElse(new NodeList<>());
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

        if(rightHandOperand.isType(KeywordCall.class)){
            return Optional.of((KeywordCall) rightHandOperand.getDefinition());
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
    public double distance(SourceNode other) {
        if(other == this){
            return 0.;
        }

        if(!(other instanceof Assignment)){
            return 1.;
        }

        Assignment assignment = (Assignment)other;

        double distReturn = LevenshteinDistance.index(this.leftHandOperand, assignment.leftHandOperand) * 0.2;

        double distExpression;
        if(rightHandOperand == null){
            distExpression = assignment.rightHandOperand == null ? 0. : 0.8;
        }
        else{
            distExpression = rightHandOperand.distance(assignment.rightHandOperand) * 0.8;
        }

        return distReturn + distExpression;
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

        if(other instanceof Assignment){
            Assignment assignment = (Assignment)other;
            edits.addAll(LevenshteinDistance.getDifferences(this.leftHandOperand, assignment.leftHandOperand));
            getKeywordCall().ifPresent(call -> edits.addAll(call.differences(assignment.getKeywordCall().orElse(null))));
        }
        else if(other instanceof KeywordCall){
            edits.addAll(LevenshteinDistance.getDifferences(this.leftHandOperand, new NodeList<>()));
            getKeywordCall().ifPresent(call -> edits.addAll(call.differences(other)));
        }
        else {
            edits.add(Edit.changeType(this, other));
        }

        return edits;
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

    public boolean isDefinition(Variable variable){
        for(Variable candidate: leftHandOperand){
            if(candidate.matches(variable.getNameToken())){
                return true;
            }
        }

        return false;
    }
}
