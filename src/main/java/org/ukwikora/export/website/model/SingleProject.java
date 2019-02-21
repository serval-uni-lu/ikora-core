package org.ukwikora.export.website.model;

import org.ukwikora.model.Project;

public class SingleProject {
    private final Link link;
    private final int numberKeywords;
    private final int numberTestCases;
    private final int linesOfCode;

    public SingleProject(Project project) throws Exception {
        this.link = new Link(project.getName());
        this.numberKeywords = project.getUserKeywords().size();
        this.numberTestCases = project.getTestCases().size();
        this.linesOfCode = project.getLoc();
    }

    public Link getLink() {
        return link;
    }

    public int getNumberKeywords() {
        return numberKeywords;
    }

    public int getNumberTestCases() {
        return numberTestCases;
    }

    public int getLinesOfCode() {
        return linesOfCode;
    }
}
