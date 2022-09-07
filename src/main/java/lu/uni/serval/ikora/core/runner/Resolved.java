package lu.uni.serval.ikora.core.runner;

import lu.uni.serval.ikora.core.model.SourceNode;

public class Resolved {
    final String value;
    final SourceNode node;

    public Resolved(String value, SourceNode node) {
        this.value = value;
        this.node = node;
    }

    public String getValue() {
        return value;
    }

    public SourceNode getNode() {
        return node;
    }
}
