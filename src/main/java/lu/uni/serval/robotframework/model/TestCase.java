package lu.uni.serval.robotframework.model;

import javax.annotation.Nonnull;
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

    @Override
    public boolean equals(Object other) {
        if(other instanceof TestCase){
            return ((TestCase)other).file.equals(this.file)
                    && ((TestCase)other).name == this.name
                    && ((TestCase)other).steps == this.steps;

        }

        return super.equals(other);
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

    @Nonnull
    public Iterator<Step> iterator() {
        return steps.iterator();
    }
}
