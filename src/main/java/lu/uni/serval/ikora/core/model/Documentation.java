package lu.uni.serval.ikora.core.model;

import lu.uni.serval.ikora.core.analytics.difference.Edit;
import lu.uni.serval.ikora.core.analytics.visitor.NodeVisitor;
import lu.uni.serval.ikora.core.analytics.visitor.VisitorMemory;
import lu.uni.serval.ikora.core.exception.RunnerException;
import lu.uni.serval.ikora.core.runner.Runtime;

import java.util.Collections;
import java.util.List;

public class Documentation extends SourceNode{
    Token label;
    Tokens text;

    public Documentation(){
        this.label = Token.empty();
        this.text = Tokens.empty();
    }

    public Documentation(Token label, Tokens text){
        this.label = label.setType(Token.Type.LABEL);
        this.text = text.setType(Token.Type.DOCUMENTATION);

        addToken(this.label);
        addTokens(this.text);
    }

    public boolean isPresent(){
        return !this.label.isEmpty();
    }

    public boolean isEmpty(){
        return isPresent() && this.text.isEmpty();
    }

    @Override
    public String toString(){
        return text.clean().toString();
    }

    @Override
    public boolean matches(Token name) {
        return this.text.first().equalsIgnorePosition(name);
    }

    @Override
    public void accept(NodeVisitor visitor, VisitorMemory memory) {
        visitor.visit(this, memory);
    }

    @Override
    public void execute(Runtime runtime) throws RunnerException {
        //nothing to do
    }

    @Override
    public Token getNameToken() {
        return text.first();
    }

    @Override
    public double distance(SourceNode other) {
        return 0;
    }

    @Override
    public List<Edit> differences(SourceNode other) {
        if(other == null){
            return Collections.singletonList(Edit.addDocumentation(this));
        }

        if(other instanceof Documentation){
            Documentation that = (Documentation) other;

            if(this.text.isEmpty() && !that.text.isEmpty()){
                return Collections.singletonList(Edit.addDocumentation(that));
            }
            else if(!this.text.isEmpty() && that.text.isEmpty()){
                return Collections.singletonList(Edit.removeDocumentation(this));
            }
            else if(!this.text.equalsIgnorePosition(that.text)){
                return Collections.singletonList(Edit.changeDocumentation(this, that));
            }
        }

        return Collections.emptyList();
    }
}
