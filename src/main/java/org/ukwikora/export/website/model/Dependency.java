package org.ukwikora.export.website.model;

public class Dependency {
    public enum Type{
        UserProject,
        Library
    }

    private final String source;
    private final String target;
    private final String type;

    public Dependency(String source, String target, Type type) {
        this.source = source;
        this.target = target;
        this.type = type.toString();
    }

    public String getSource() {
        return source;
    }

    public String getTarget() {
        return target;
    }

    public String getType() {
        return type;
    }
}
