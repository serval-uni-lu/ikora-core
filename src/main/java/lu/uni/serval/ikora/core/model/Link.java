package lu.uni.serval.ikora.core.model;

import java.util.*;

public class Link<K extends SourceNode,T extends Dependable> {
    public enum Import{
        STATIC,
        DYNAMIC,
        BOTH
    }

    private final K source;
    private final Set<T> staticCallee;
    private final Set<T> dynamicCallee;

    public Link(K source) {
        this.source = source;
        this.staticCallee = new HashSet<>();
        this.dynamicCallee = new HashSet<>();
    }

    public K getSource() {
        return source;
    }

    public Optional<T> getNode(){
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

    public void addNode(T destination, Import importType) {
        if(destination == null){
            return;
        }

        addNode(destination, importType == Import.STATIC ? staticCallee: dynamicCallee);
        destination.addDependency(source);
    }

    private void addNode(T destination, Set<T> destinations) {
        destinations.add(destination);
    }
}
