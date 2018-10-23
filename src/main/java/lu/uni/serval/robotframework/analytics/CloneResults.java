package lu.uni.serval.robotframework.analytics;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lu.uni.serval.robotframework.model.Element;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@JsonSerialize(using = CloneResultSerializer.class)
public class CloneResults<T extends Element> {
    private Map<Difference.Clone, Map<T, Set<T>>> clones;

    public CloneResults(){
        this.clones = new HashMap<>();
    }

    public void update(T t1, T t2, Difference.Clone clone){
        set(t1, t2, clone);
        set(t2, t1, clone);
    }

    private void set(T t1, T t2, Difference.Clone clone){
        Map<T, Set<T>> values = this.clones.getOrDefault(clone, new HashMap<>());
        Set<T> clones = values.getOrDefault(t1, new HashSet<>());
        clones.add(t2);

        values.put(t1, clones);
        this.clones.put(clone, values);
    }

    public Difference.Clone getCloneType(T element) {
        if(clones.getOrDefault(Difference.Clone.TypeI, new HashMap<>()).get(element) != null){
            return Difference.Clone.TypeI;
        }
        else if(clones.getOrDefault(Difference.Clone.TypeII, new HashMap<>()).get(element) != null){
            return Difference.Clone.TypeII;
        }

        return Difference.Clone.None;
    }

    public int size(Difference.Clone clone) {
        Map<T, Set<T>> types = this.clones.get(clone);

        if(types == null){
            return 0;
        }

        return types.size();
    }
}
