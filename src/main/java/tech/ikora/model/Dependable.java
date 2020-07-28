package tech.ikora.model;

import java.util.Set;

public interface Dependable extends Node {
    void addDependency(SourceNode node);
    void removeDependency(SourceNode node);
    Set<SourceNode> getDependencies();
}
