package org.ukwikora.analytics;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.ukwikora.export.json.CloneResultSerializer;
import org.ukwikora.model.Statement;

import javax.annotation.Nonnull;
import java.util.*;

@JsonSerialize(using = CloneResultSerializer.class)
public class Clones<T extends Statement> implements Iterable<Clone<T>> {
    private Map<Clone.Type, Map<T, Clone<T>>> clones;

    public Clones(){
        this.clones = new EnumMap<>(Clone.Type.class);

        this.clones.put(Clone.Type.TypeI, new HashMap<>());
        this.clones.put(Clone.Type.TypeII, new HashMap<>());
        this.clones.put(Clone.Type.TypeIII, new HashMap<>());
        this.clones.put(Clone.Type.TypeIV, new HashMap<>());
    }

    public void update(T t1, T t2, Clone.Type cloneType){
        if(Clone.Type.None == cloneType){
            return;
        }

        set(t1, t2, cloneType);
        set(t2, t1, cloneType);
    }

    private void set(T t1, T t2, Clone.Type type){
        Map<T, Clone<T>> clones = this.clones.get(type);

        Clone<T> clone = clones.getOrDefault(t1, new Clone<>(t1, type));
        clone.addClone(t2);
        clones.put(t1, clone);
    }

    public Clone.Type getCloneType(T element) {
        if(clones.getOrDefault(Clone.Type.TypeI, new HashMap<>()).get(element) != null){
            return Clone.Type.TypeI;
        }
        else if(clones.getOrDefault(Clone.Type.TypeII, new HashMap<>()).get(element) != null){
            return Clone.Type.TypeII;
        }

        return Clone.Type.None;
    }

    public int size(Clone.Type type) {
        Map<T, Clone<T>> clones = this.clones.getOrDefault(type, Collections.emptyMap());
        return clones.size();
    }

    @Override
    @Nonnull
    public Iterator<Clone<T>> iterator() {
        return new CloneIterator();
    }

    class CloneIterator implements Iterator<Clone<T>>{
        private Clone.Type currentType;
        private Iterator<Map.Entry<T, Clone<T>>> currentIterator;

        CloneIterator(){
            this.currentType = Clone.Type.TypeI;
            this.currentIterator = getIterator(this.currentType);
        }

        @Override
        public boolean hasNext() {
            if(currentIterator.hasNext()){
                return true;
            }

            Clone.Type type = getNextType(currentType);
            Iterator<Map.Entry<T, Clone<T>>> iterator = getIterator(type);

            while (iterator != null){
                if(iterator.hasNext()){
                    return true;
                }

                type = getNextType(type);
                iterator = getIterator(type);
            }

            return false;
        }

        @Override
        public Clone<T> next() {
            if(currentIterator.hasNext()){
                return currentIterator.next().getValue();
            }

            currentType = getNextType(currentType);
            currentIterator = getIterator(this.currentType);

            while (currentIterator != null){
                if(currentIterator.hasNext()){
                    return currentIterator.next().getValue();
                }

                currentType = getNextType(currentType);
                currentIterator = getIterator(currentType);
            }

            return null;
        }

        private Iterator<Map.Entry<T, Clone<T>>> getIterator(Clone.Type type){
            if(Clone.Type.None == type){
                return null;
            }

            return Clones.this.clones.get(type).entrySet().iterator();
        }

        private Clone.Type getNextType(Clone.Type type){
            switch (type){
                case TypeI: return Clone.Type.TypeII;
                case TypeII: return Clone.Type.TypeIII;
                case TypeIII: return Clone.Type.TypeIV;
                case TypeIV:
                case None:
                default: return Clone.Type.None;
            }
        }
    }
}
