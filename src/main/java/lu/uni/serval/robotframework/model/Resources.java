package lu.uni.serval.robotframework.model;

import java.util.List;

public class Resources {
    enum Type{
        Resource,Library, Unknown
    }

    private Type type;
    private String name;
    private List<String> arguments;
    private String comment;
    private TestCaseFile resourcesFile;

    public Resources(Type type, String name, List<String> arguments, String comment) {
        this.type = type;
        this.name = name;
        this.arguments = arguments;
        this.comment = comment;
    }

    public Type getType() {
        return this.type;
    }

    public String getName() {
        return name;
    }

    public void setResourcesFile(TestCaseFile resourcesFile) {
        this.resourcesFile = resourcesFile;
    }
}
