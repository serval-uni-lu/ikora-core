package lu.uni.serval.analytics;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lu.uni.serval.robotframework.model.Element;
import lu.uni.serval.utils.CompareCache;
import lu.uni.serval.utils.tree.LabelTreeNode;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@JsonSerialize(using = CloneResultSerializer.class)
public class CloneResults<T extends Element> {
    enum Type{
        TypeI, TypeII, TypeIII, TypeIV, None
    }

    private Map<Type, Map<T, Set<T>>> clones;
    private Map<Type, Set<Set<T>>> clusters;

    public CloneResults(){
        this.clones = new HashMap<>();
        this.clusters = null;
    }

    public void update(T t1, T t2, Type type){
        set(t1, t2, type);
        set(t2, t1, type);
    }

    private void set(T t1, T t2, Type type){
        Map<T, Set<T>> values = this.clones.getOrDefault(type, new HashMap<>());
        Set<T> clones = values.getOrDefault(t1, new HashSet<>());
        clones.add(t2);

        values.put(t1, clones);
        this.clones.put(type, values);
    }
}
