package tech.ikora.model;

import tech.ikora.analytics.Action;
import tech.ikora.analytics.visitor.NodeVisitor;
import tech.ikora.analytics.visitor.VisitorMemory;
import tech.ikora.builder.SymbolResolver;
import tech.ikora.runner.Runtime;
import tech.ikora.utils.LevenshteinDistance;

import java.util.*;

public class KeywordCall extends Step {
    private final Link<KeywordCall, Keyword> link;
    private final NodeList<Argument> arguments;

    private NodeList<Value> returnValues;
    private Gherkin gherkin;

    public KeywordCall(Token name) {
        super(name);
        addToken(name);

        this.gherkin = Gherkin.none();
        this.link = new Link<>(this);

        this.arguments = new NodeList<>();
        this.addAstChild(this.arguments);
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

    @Override
    public NodeList<Argument> getArgumentList() {
        return this.arguments;
    }

    public void setArgumentList(NodeList<Argument> argumentList) {
        this.arguments.clear();

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

    public Keyword.Type getKeywordType(){
        return link.getNode().map(Keyword::getType).orElse(Keyword.Type.NONE);
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

            List<Action> argumentActions = LevenshteinDistance.getDifferences(this.arguments, call.arguments);
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

    public NodeList<Value> getReturnValues() {
        return this.returnValues;
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

    private void addArgument(Argument argument) {
        this.arguments.add(argument);

        addTokens(argument.getTokens());
        argument.getDefinition().addDependency(this);
    }

    private void clearArguments(){
        for(Argument argument: this.arguments){
            argument.getDefinition().removeDependency(this);
        }

        this.arguments.clear();
    }
}
