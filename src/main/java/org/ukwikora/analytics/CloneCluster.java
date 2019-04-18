package org.ukwikora.analytics;

import org.ukwikora.model.Statement;

import java.util.Set;

public class CloneCluster<T extends Statement> {
    private final Clone.Type type;
    private final Set<T> clones;

    public CloneCluster(Clone.Type type, Set<T> clones){
        this.type = type;
        this.clones = clones;
    }

    public Clone.Type getType() {
        return type;
    }

    public Set<T> getClones() {
        return clones;
    }

    public boolean containsAll(CloneCluster other) {
        if(this.type != other.type){
            return false;
        }

        return this.clones.containsAll(other.clones);
    }

    @Override
    public boolean equals(Object obj) {
        if(obj.getClass() != this.getClass()){
            return false;
        }

        CloneCluster<T> other = (CloneCluster<T>)obj;

        return  other.type == this.type && other.getClones().equals(this.getClones());
    }

    @Override
    public int hashCode() {
        int hash = 1;
        hash = hash * 17 + type.hashCode();
        hash = hash * 31 + clones.hashCode();

        return hash;
    }
}
