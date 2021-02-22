package lu.uni.serval.ikora.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ResourcesTable implements Iterable<Resources> {
    private final List<Resources> resourcesList = new ArrayList<>();

    public Iterator<Resources> iterator() {
        return resourcesList.iterator();
    }

    public void add(Resources resources) {
        resourcesList.add(resources);
    }
}
