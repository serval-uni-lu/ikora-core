package tech.ikora.model;

import java.util.List;

public class VariableFile {
    private final String path;
    private final List<String> parameters;

    public VariableFile(String path, List<String> parameters) {
        this.path = path;
        this.parameters = parameters;
    }
}
