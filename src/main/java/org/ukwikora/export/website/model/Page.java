package org.ukwikora.export.website.model;

public class Page {
    private final String id;
    private final String name;

    public Page(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
