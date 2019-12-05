package org.ikora.model;

import java.util.List;

public class Library {
    private String name;
    private List<String> arguments;
    private String comment;

    public Library(String name, List<String> arguments, String comment) {
        this.name = name;
        this.arguments = arguments;
        this.comment = comment;
    }

    public String getName() {
        return this.name;
    }
}
