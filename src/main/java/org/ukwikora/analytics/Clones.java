package org.ukwikora.analytics;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.ukwikora.export.CloneResultSerializer;
import org.ukwikora.model.Element;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@JsonSerialize(using = CloneResultSerializer.class)
public class Clones<T extends Element> {
    public enum Type{
        TypeI, TypeII, TypeIII, TypeIV, None
    }


    private Map<Type, Map<T, Set<T>>> clones;

    public Clones(){
        this.clones = new HashMap<>();
    }

    public void update(T t1, T t2, Type cloneType){
        set(t1, t2, cloneType);
        set(t2, t1, cloneType);
    }

    private void set(T t1, T t2, Type clone){
        Map<T, Set<T>> values = this.clones.getOrDefault(clone, new HashMap<>());
        Set<T> clones = values.getOrDefault(t1, new HashSet<>());
        clones.add(t2);

        values.put(t1, clones);
        this.clones.put(clone, values);
    }

    public Type getCloneType(T element) {
        if(clones.getOrDefault(Type.TypeI, new HashMap<>()).get(element) != null){
            return Type.TypeI;
        }
        else if(clones.getOrDefault(Type.TypeII, new HashMap<>()).get(element) != null){
            return Type.TypeII;
        }

        return Type.None;
    }

    public int size(Type clone) {
        Map<T, Set<T>> types = this.clones.get(clone);

        if(types == null){
            return 0;
        }

        return types.size();
    }
}
