package org.ukwikora.analytics;

import org.ukwikora.model.Statement;

import java.util.HashSet;
import java.util.Set;

public class Clone<T extends Statement> {

    public enum Type{
        TypeI, TypeII, TypeIII, TypeIV, None
    }

    private final T statement;
    private final Type type;
    private Set<T> clones;

    Clone(T statement, Type type){
        this.statement = statement;
        this.type = type;
        this.clones = new HashSet<>();
    }

    public T getStatement() {
        return statement;
    }

    public Type getType() {
        return type;
    }

    public Set<T> getClones() {
        return clones;
    }

    public Set<T> getAll() {
        Set<T> all = new HashSet<>(clones);
        all.add(statement);

        return all;
    }

    public int getNumberClones() {
        return clones.size();
    }

    public boolean addClone(T statement){
        return clones.add(statement);
    }
}
