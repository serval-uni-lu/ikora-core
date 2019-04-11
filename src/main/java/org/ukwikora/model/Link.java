package org.ukwikora.model;

import java.util.*;

import org.ukwikora.exception.InvalidDependencyException;
import org.ukwikora.exception.InvalidImportTypeException;

public class Link<K extends Statement,T extends Statement> {
    public enum Import{
        STATIC,
        DYNAMIC,
        BOTH
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

    public Set<T> getAllLinks(Import importType){
        Set<T> allLinks = new HashSet<>();

        switch (importType) {
            case STATIC:
                allLinks.addAll(staticCallee);
                break;
            case DYNAMIC:
                allLinks.addAll(dynamicCallee);
                break;
            case BOTH:
                allLinks.addAll(staticCallee);
                allLinks.addAll(dynamicCallee);
                break;
        }

        return allLinks;
    }

    public void addStatement(T destination, Import importType)
            throws InvalidImportTypeException, InvalidDependencyException {
        switch (importType) {
            case STATIC:
                addStatement(destination, staticCallee);
                break;
            case DYNAMIC:
                addStatement(destination, dynamicCallee);
                break;
            case BOTH:
                throw new InvalidImportTypeException("Was expecting a STATIC or DYNAMIC import type, got BOTH instead");
        }
    }

    private void addStatement(T destination, Set<T> destinations) throws InvalidDependencyException {
        if(destination == null){
            return;
        }

        destinations.add(destination);
        destination.addDependency(source);
    }
}
