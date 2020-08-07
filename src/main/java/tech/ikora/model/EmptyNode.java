package tech.ikora.model;

import tech.ikora.analytics.Edit;
import tech.ikora.analytics.visitor.NodeVisitor;
import tech.ikora.analytics.visitor.VisitorMemory;
import tech.ikora.runner.Runtime;

import java.util.Collections;
import java.util.List;

public class EmptyNode extends SourceNode {
    public EmptyNode(SourceNode parent){
        setOneWayParent(parent);
    }

    @Override
    public Token getNameToken() {
        return Token.empty();
    }

    @Override
    public double distance(SourceNode other) {
        return 1.;
    }

    @Override
    public List<Edit> differences(SourceNode other) {
        if(other instanceof EmptyNode){
            return Collections.emptyList();
        }

        return Collections.singletonList(Edit.addElement(other.getClass(), this, other));
    }

    @Override
    public boolean matches(Token name) {
        return false;
    }

    @Override
    public void accept(NodeVisitor visitor, VisitorMemory memory) {
        //this is a null node, it should not accept anything
    }

    @Override
    public void execute(Runtime runtime) throws Exception {

    }
}
