package lu.uni.serval.ikora.core.model;

import lu.uni.serval.ikora.core.analytics.difference.Edit;
import lu.uni.serval.ikora.core.analytics.visitor.NodeVisitor;
import lu.uni.serval.ikora.core.analytics.visitor.VisitorMemory;
import lu.uni.serval.ikora.core.exception.InvalidTypeException;
import lu.uni.serval.ikora.core.runner.Runtime;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class InvalidStep extends Step {
    public InvalidStep(Token name) {
        super(name);
    }

    @Override
    public Optional<KeywordCall> getKeywordCall() {
        return Optional.empty();
    }

    @Override
    public NodeList<Argument> getArgumentList() {
        return new NodeList<>();
    }

    @Override
    public boolean hasParameters() {
        return false;
    }

    @Override
    public void accept(NodeVisitor visitor, VisitorMemory memory) {
        // should not be visited
    }

    @Override
    public void execute(Runtime runtime) throws Exception {
        throw new InvalidTypeException("Invalid step cannot be executed");
    }

    @Override
    public double distance(SourceNode other) {
        return other instanceof InvalidStep ? 0.0 : 1.0;
    }

    @Override
    public List<Edit> differences(SourceNode other) {
        List<Edit> edits = new ArrayList<>();

        if(!(other instanceof InvalidStep)){
            edits.add(Edit.changeType(this, other));
        }

        return edits;
    }
}
