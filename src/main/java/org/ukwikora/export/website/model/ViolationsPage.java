package org.ukwikora.export.website.model;

import org.ukwikora.model.Project;

import java.util.List;

public class ViolationsPage extends Page{
    private final Table table;

    public ViolationsPage(String id, String name, List<Project> projects) {
        super(id, name);

        this.table = new Table(
          "violations",
          "Violations",
          new String[]{"Type", "Name", "File", "Line", "Message"}
        );
    }
}
