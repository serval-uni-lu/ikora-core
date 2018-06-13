package lu.uni.serval.robotframework.model;

import java.util.List;

public class Resources {
    public enum Type{
        Resource,Library, Unknown
    }

    private Type type;
    private String name;
    private List<String> arguments;
    private String comment;
    private TestCaseFile file;

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
        return this.name;
    }

    public TestCaseFile getFile() {
        return this.file;
    }

    public void setFile(TestCaseFile file) {
        this.file = file;
    }
}
