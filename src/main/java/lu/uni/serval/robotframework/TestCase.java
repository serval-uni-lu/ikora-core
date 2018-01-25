package lu.uni.serval.robotframework;

import java.util.List;

public class TestCase {
    private String name;
    private String documentation;
    private List<Step> steps;

    public TestCase(String name, String documentation, List<Step> steps) {
        this.name = name;
        this.documentation = documentation;
        this.steps = steps;
    }
}
