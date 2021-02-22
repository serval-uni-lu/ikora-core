package lu.uni.serval.ikora.model;

import lu.uni.serval.ikora.analytics.difference.Edit;
import lu.uni.serval.ikora.analytics.visitor.NodeVisitor;
import lu.uni.serval.ikora.analytics.visitor.VisitorMemory;

import java.util.ArrayList;
import java.util.List;

public class InvalidVariable extends Variable {
    public InvalidVariable() {
        super(Token.fromString("INVALID"));
    }

    @Override
    protected void setName(Token name) {
        this.name = name;
    }

    @Override
    public void accept(NodeVisitor visitor, VisitorMemory memory) {
        //nothing to do
    }

    @Override
    public double distance(SourceNode other) {
        if(other instanceof InvalidVariable){
            return 0.0;
        }

        return 1.0;
    }

    @Override
    public List<Edit> differences(SourceNode other) {
        List<Edit> edits = new ArrayList<>();

        if(!(other instanceof InvalidStep)){
            edits.add(Edit.changeVariableDefinition(this, other));
        }

        return edits;
    }
}
