package lu.uni.serval.ikora.core.model;

import lu.uni.serval.ikora.core.analytics.difference.Edit;
import lu.uni.serval.ikora.core.analytics.visitor.NodeVisitor;
import lu.uni.serval.ikora.core.analytics.visitor.VisitorMemory;
import lu.uni.serval.ikora.core.builder.SymbolResolver;
import lu.uni.serval.ikora.core.runner.Runtime;
import lu.uni.serval.ikora.core.utils.LevenshteinDistance;

import java.util.*;

public class KeywordCall extends Step {
    private final Link<KeywordCall, Keyword> link;
    private final NodeList<Argument> arguments;

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
        if(keyword == null){
            throw new NullPointerException("Cannot link a keywordCall to a null Keyword");
        }

        link.addNode(keyword, importLink);
    }

    @Override
    public NodeList<Argument> getArgumentList() {
        return this.arguments;
    }

    public void setArgumentList(NodeList<Argument> argumentList) {
        this.clearArguments();

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
    public double distance(SourceNode other) {
        if(other == null){
            return 1.;
        }

        if (!(other instanceof KeywordCall)){
            return 1.;
        }

        KeywordCall call = (KeywordCall)other;

        double distName = this.getNameToken().equalsIgnorePosition(call.getNameToken()) ? 0. : 0.5;
        double distArguments = LevenshteinDistance.index(this.arguments, call.arguments);

        return distName + distArguments;
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

        if(other instanceof KeywordCall){
            KeywordCall call = (KeywordCall)other;

            if(!this.getNameToken().equalsIgnorePosition(call.getNameToken())){
                edits.add(Edit.changeStepName(this, call));
            }

            List<Edit> argumentEdits = LevenshteinDistance.getDifferences(this.arguments, call.arguments);
            edits.addAll(argumentEdits);
        }
        else if(other instanceof Assignment){
            final Assignment assignment = (Assignment)other;

            edits.addAll(this.differences(assignment.getKeywordCall().orElse(null)));
            edits.addAll(LevenshteinDistance.getDifferences(new NodeList<>(), assignment.getLeftHandOperand()));
        }
        else{
            edits.add(Edit.changeType(this, other));
        }

        return edits;
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
    }

    private void clearArguments(){
        this.arguments.clear();
    }
}
