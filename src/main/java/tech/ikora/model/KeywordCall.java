package tech.ikora.model;

import tech.ikora.analytics.Action;
import tech.ikora.analytics.visitor.NodeVisitor;
import tech.ikora.analytics.visitor.VisitorMemory;
import tech.ikora.builder.SymbolResolver;
import tech.ikora.runner.Runtime;
import tech.ikora.utils.LevenshteinDistance;

import java.util.*;
import java.util.stream.Collectors;

public class KeywordCall extends Step {
    private Link<KeywordCall, Keyword> link;
    private List<Variable> returnVariables;
    private Gherkin gherkin;

    public KeywordCall(Token name) {
        super(name);
        addToken(name);

        this.gherkin = Gherkin.none();
        this.link = new Link<>(this);
    }

    public void setGherkin(Gherkin gherkin) {
        this.gherkin = gherkin;
        addToken(this.gherkin.getToken());
    }

    public Gherkin getGherkin(){
        return gherkin;
    }

    public void linkKeyword(Keyword keyword, Link.Import importLink) {
        link.addNode(keyword, importLink);
    }

    public void addArgument(Argument argument) {
        this.addAstChild(argument);
        addTokens(argument.getTokens());

        argument.getDefinition().ifPresent(node -> node.addDependency(this));
    }

    @Override
    public ArgumentList getArgumentList() {
        final List<Argument> arguments = getAstChildren().stream()
                .map(node -> (Argument) node)
                .collect(Collectors.toList());

        return new ArgumentList(arguments);
    }

    public void setArgumentList(ArgumentList argumentList) {
        this.getAstChildren().clear();

        for(Argument argument: argumentList){
            addArgument(argument);
        }
    }

    @Override
    public boolean hasParameters() {
        return !getAstChildren().isEmpty();
    }

    public Optional<Keyword> getKeyword() {
        return link.getNode();
    }

    public Set<Keyword> getAllPotentialKeywords(Link.Import importType){
        return link.getAllLinks(importType);
    }

    @Override
    public void execute(Runtime runtime) throws Exception{
        runtime.enterNode(this);
        SymbolResolver.resolve(this, runtime);

        Optional<Keyword> callee = link.getNode();

        if(callee.isPresent()){
            callee.get().execute(runtime);
            returnVariables = runtime.getReturnVariables();
        }
        else{
            throw new Exception("Need to have a better exception");
        }

        runtime.exitNode(this);
    }

    @Override
    public double distance(Differentiable other) {
        if(other == null){
            return 1.;
        }

        if (!(other instanceof KeywordCall)){
            return 1.;
        }

        SourceNode sourceNode = (SourceNode)other;

        double distName = this.getNameToken().equalsIgnorePosition(sourceNode.getNameToken()) ? 0. : 0.5;

        boolean sameArguments = this.getAstChildren().size() == sourceNode.getAstChildren().size();
        for(int i = 0; sameArguments && i < this.getAstChildren().size(); ++i) {
            sameArguments = this.getAstChildren().get(i).equals(sourceNode.getAstChildren().get(i));
        }

        double distArguments = sameArguments ? 0. : 0.5;

        return distName + distArguments;
    }

    @Override
    public List<Action> differences(Differentiable other) {
        if(other == this){
            return Collections.emptyList();
        }

        if(other == null){
            return Collections.singletonList(Action.addElement(this.getClass(), this));
        }

        List<Action> actions = new ArrayList<>();

        if(other instanceof KeywordCall){
            KeywordCall call = (KeywordCall)other;

            if(!this.getNameToken().equalsIgnorePosition(call.getNameToken())){
                actions.add(Action.changeStepName(this, call));
            }

            List<Action> argumentActions = LevenshteinDistance.getDifferences(this.getAstChildren(), call.getAstChildren());
            actions.addAll(argumentActions);
        }
        else if(other instanceof Assignment){
            Assignment assignment = (Assignment)other;
            actions.addAll(this.differences(assignment.getKeywordCall().orElse(null)));
            actions.addAll(LevenshteinDistance.getDifferences(Collections.emptyList(), assignment.getLeftHandOperand()));
        }
        else{
            actions.add(Action.changeType(this, other));
        }

        return actions;
    }

    @Override
    public String toString(){
        StringBuilder builder = new StringBuilder();

        builder.append(getNameToken());

        for (Argument argument: getArgumentList()){
            builder.append("\t");
            builder.append(argument.toString());
        }

        return builder.toString();
    }

    public List<Variable> getReturnVariables() {
        return this.returnVariables;
    }

    @Override
    public void accept(NodeVisitor visitor, VisitorMemory memory){
        visitor.visit(this, memory);
    }

    @Override
    public Optional<KeywordCall> getKeywordCall() {
        if(template != null){
            return Optional.of(template);
        }

        return Optional.of(this);
    }
}
