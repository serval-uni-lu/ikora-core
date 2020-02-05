package tech.ikora.analytics;

import tech.ikora.model.Project;
import tech.ikora.model.Node;

import java.util.HashSet;
import java.util.Set;

public class CloneCluster<T extends Node> {
    private final Clone.Type type;
    private final Set<T> clones;
    private final Set<Project> projects;

    public CloneCluster(Clone.Type type, Set<T> clones){
        this.type = type;
        this.clones = clones;

        this.projects = new HashSet<>();

        for(T clone: clones){
            if(clone.getProject() != null){
                this.projects.add(clone.getProject());
            }
        }
    }

    public Clone.Type getType() {
        return type;
    }

    public boolean isCrossProject(){
        return this.projects.size() > 1;
    }

    public Set<T> getClones() {
        return clones;
    }

    public boolean containsAll(CloneCluster<T> other) {
        if(this.type != other.type){
            return false;
        }

        return this.clones.containsAll(other.clones);
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == null){
            return false;
        }

        if(this == obj){
            return true;
        }

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

    public int size() {
        return clones.size();
    }
}
