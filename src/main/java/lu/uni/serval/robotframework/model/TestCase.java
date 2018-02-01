package lu.uni.serval.robotframework.model;

import java.util.Iterator;
import java.util.List;

public class TestCase implements Iterable<Step> {
    private String file;
    private Argument name;
    private String documentation;
    private List<Step> steps;

    public TestCase(String file, String name, String documentation, List<Step> steps) {
        this.file = file;
        this.name = new Argument(name);
        this.documentation = documentation;
        this.steps = steps;
    }

    public String getFile() {
        return file;
    }

    public Argument getName() {
        return name;
    }

    public String getDocumentation() {
        return documentation;
    }

    public List<Step> getSteps() {
        return steps;
    }

    public Iterator<Step> iterator() {
        return steps.iterator();
    }
}
