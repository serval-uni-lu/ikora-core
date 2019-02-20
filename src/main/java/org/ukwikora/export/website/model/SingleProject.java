package org.ukwikora.export.website.model;

import org.ukwikora.model.Project;

public class SingleProject {
    private final Link link;

    public SingleProject(Project project) throws Exception {
        this.link = new Link(project.getName());
    }

    public Link getLink() {
        return link;
    }
}
