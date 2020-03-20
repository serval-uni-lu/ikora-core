package tech.ikora.analytics.clones;

import tech.ikora.model.Node;

import java.util.HashSet;
import java.util.Set;

public class Clone<T extends Node> {

    public enum Type{
        TYPE_1, TYPE_2, TYPE_3, TYPE_4, NONE
    }

    private final T node;
    private final Type type;
    private Set<T> clones;

    Clone(T node, Type type){
        this.node = node;
        this.type = type;
        this.clones = new HashSet<>();
    }

    public T getNode() {
        return node;
    }

    public Type getType() {
        return type;
    }

    public Set<T> getClones() {
        return clones;
    }

    public Set<T> getAll() {
        Set<T> all = new HashSet<>(clones);
        all.add(node);

        return all;
    }

    public int getNumberClones() {
        return clones.size();
    }

    public boolean addClone(T node){
        return clones.add(node);
    }
}
