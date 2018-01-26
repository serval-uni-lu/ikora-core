package lu.uni.serval.robotframework.model;

import java.util.List;

public class Step {
    private String file;
    private String name;
    private List<String> arguments;

    public Step(String file, String name, List<String> arguments) {
        this.file = file;
        this.name = name;
        this.arguments = arguments;
    }

    public String getFile() {
        return this.file;
    }

    public String getName() {
        return this.name;
    }

    public List<String> getArguments() {
        return this.arguments;
    }
}
