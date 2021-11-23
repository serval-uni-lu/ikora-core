package lu.uni.serval.ikora.core.model;

import lu.uni.serval.ikora.core.analytics.difference.Edit;
import lu.uni.serval.ikora.core.analytics.visitor.NodeVisitor;
import lu.uni.serval.ikora.core.analytics.visitor.VisitorMemory;
import lu.uni.serval.ikora.core.exception.RunnerException;
import lu.uni.serval.ikora.core.runner.Runtime;

import java.util.List;

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
    }

    public Phase getPhase() {
        return phase;
    }

    public Token getLabel() {
        return label;
    }

    public KeywordCall getCall() {
        return call;
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
    public double distance(SourceNode other) {
        return 0;
    }

    @Override
    public List<Edit> differences(SourceNode other) {
        return null;
    }
}
