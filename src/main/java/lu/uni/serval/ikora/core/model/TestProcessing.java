package lu.uni.serval.ikora.core.model;

import lu.uni.serval.ikora.core.analytics.difference.Edit;
import lu.uni.serval.ikora.core.analytics.visitor.NodeVisitor;
import lu.uni.serval.ikora.core.analytics.visitor.VisitorMemory;
import lu.uni.serval.ikora.core.exception.RunnerException;
import lu.uni.serval.ikora.core.runner.Runtime;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class TestProcessing extends SourceNode {
    public enum Phase {
        SETUP,
        TEARDOWN,
        TEMPLATE
    }

    private final Phase phase;
    private final Token label;
    private final KeywordCall call;

    public TestProcessing(Phase phase, Token label, KeywordCall call) {
        this.phase = phase;
        this.label = label;

        this.call = call;
        addAstChild(this.call);
    }

    public Phase getPhase() {
        return phase;
    }

    public Token getLabel() {
        return label;
    }

    public Optional<KeywordCall> getCall() {
        return Optional.ofNullable(call);
    }

    @Override
    public boolean matches(Token name) {
        return false;
    }

    @Override
    public void accept(NodeVisitor visitor, VisitorMemory memory) {
        visitor.visit(this, memory);
    }

    @Override
    public void execute(Runtime runtime) throws RunnerException {
        // execution not implemented yet
    }

    @Override
    public Token getDefinitionToken() {
        if(this.call == null){
            return Token.empty();
        }

        return this.call.getDefinitionToken();
    }

    @Override
    public List<Edit> differences(SourceNode other) {
        if(other == null) return Collections.singletonList(Edit.removeElement(TestProcessing.class, this));
        if(other == this) return Collections.emptyList();
        if(this.getClass() != other.getClass()) return Collections.singletonList(Edit.changeType(this, other));

        final List<Edit> edits = new ArrayList<>();
        final TestProcessing that = (TestProcessing) other;

        if(this.getPhase() != that.getPhase()) edits.add(Edit.changeType(this, other));
        edits.addAll(this.call.differences(that.call));

        return edits;
    }
}
