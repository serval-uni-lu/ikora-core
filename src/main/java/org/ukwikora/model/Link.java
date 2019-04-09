package org.ukwikora.model;

import java.util.*;

public class Link<K extends Statement,T extends Statement> {
    public enum Import{
        STATIC,
        DYNAMIC
    }

    private final K source;
    private Set<T> staticCallee;
    private Set<T> dynamicCallee;

    public Link(K source) {
        this.source = source;
        this.staticCallee = new HashSet<>();
        this.dynamicCallee = new HashSet<>();
    }

    public K getSource() {
        return source;
    }

    public Optional<T> getStatement(){
        if(dynamicCallee.size() == 1){
            return Optional.of(dynamicCallee.iterator().next());
        }

        if(staticCallee.size() == 1){
            return Optional.of(staticCallee.iterator().next());
        }

        return Optional.empty();
    }

    public void addStatement(T destination, Import importType){
        switch (importType) {
            case STATIC:
                addStatement(destination, staticCallee);
                break;
            case DYNAMIC:
                addStatement(destination, dynamicCallee);
                break;
        }
    }

    private void addStatement(T destination, Set<T> destinations){
        if(destination == null){
            return;
        }

        destinations.add(destination);
        destination.addDependency(source);
    }
}
