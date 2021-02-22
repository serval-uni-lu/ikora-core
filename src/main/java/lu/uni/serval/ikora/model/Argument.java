package lu.uni.serval.ikora.model;

import lu.uni.serval.ikora.analytics.difference.Edit;
import lu.uni.serval.ikora.analytics.visitor.NodeVisitor;
import lu.uni.serval.ikora.analytics.visitor.VisitorMemory;
import lu.uni.serval.ikora.builder.ValueResolver;
import lu.uni.serval.ikora.runner.Runtime;

import java.util.Collections;
import java.util.List;

public class Argument extends SourceNode implements HiddenAstNode {
    private final SourceNode definition;
    private final Token name;

    public Argument(SourceNode definition) {
        if(definition == null){
            throw new NullPointerException("Argument cannot be initialize with null value");
        }

        this.name = definition.getNameToken();
        this.definition = definition;
        addTokens(this.definition.getTokens());

        this.addAstChild(this.definition);
    }

    public SourceNode getDefinition() {
        return this.definition;
    }

    public boolean isScalarVariable(){
        return isType(ScalarVariable.class);
    }

    public boolean isDictionaryVariable(){
        return isType(DictionaryVariable.class);
    }

    public boolean isDictionaryEntry(){
        return isType(DictionaryEntry.class);
    }

    public boolean isListVariable(){
        return isType(ListVariable.class);
    }

    public boolean isLiteral(){
        return isType(Literal.class);
    }

    public boolean isKeywordCall(){
        return isType(KeywordCall.class);
    }

    public boolean isVariable(){
        return isType(Variable.class);
    }

    @Override
    public boolean matches(Token name) {
        return ValueResolver.matches(this.name, name);
    }

    @Override
    public void accept(NodeVisitor visitor, VisitorMemory memory) {
        visitor.visit(this, memory);
    }

    @Override
    public void execute(Runtime runtime) {
        throw new UnsupportedOperationException();
    }

    @Override
    public double distance(SourceNode other) {
        if(other == this){
            return 0.0;
        }

        if(other == null || !Argument.class.isAssignableFrom(other.getClass())){
            return 1.0;
        }

        Argument argument = (Argument)other;

        boolean sameCall = true;
        if(this.definition != null && this.definition != argument.definition){
            sameCall = this.definition.distance(argument.definition) == 0.0;
        }

        boolean sameName = this.name.equalsIgnorePosition(argument.getNameToken());

        return sameName && sameCall ? 0.0 : 1.0;
    }

    @Override
    public List<Edit> differences(SourceNode other) {
        if(other == null){
            return Collections.singletonList(Edit.removeElement(this.getClass(), this));
        }

        if(other == this){
            return Collections.emptyList();
        }

        if(!(other instanceof Argument)){
            return Collections.singletonList(Edit.changeType(this, other));
        }

        return this.getDefinition().differences(((Argument) other).getDefinition());
    }

    @Override
    public Token getNameToken() {
        return this.name;
    }

    @Override
    public String toString() {
        if(name != null){
            return name.toString();
        }

        return "<ARGUMENT>";
    }

    public boolean isType(Class<?> type){
        return type.isAssignableFrom(this.getDefinition().getClass());
    }

    public String getType(){
        return this.getDefinition().getClass().getSimpleName();
    }
}
