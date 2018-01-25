package lu.uni.serval.robotframework;

import java.util.List;

public class Resources {
    private String name;
    private List<String> arguments;
    private String comment;
    private TestCaseFile resourcesFile;

    public Resources(String name, List<String> arguments, String comment) {
        this.name = name;
        this.arguments = arguments;
        this.comment = comment;
    }

    public String getName() {
        return name;
    }

    public void setResourcesFile(TestCaseFile resourcesFile) {
        this.resourcesFile = resourcesFile;
    }
}
