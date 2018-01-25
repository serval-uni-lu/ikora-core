package lu.uni.serval.robotframework;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ResourcesTable implements Iterable<Resources> {
    private final List<Resources> resourcesList = new ArrayList<Resources>();


    public Iterator<Resources> iterator() {
        return resourcesList.iterator();
    }

    public void add(Resources resources) {
        resourcesList.add(resources);
    }
}
