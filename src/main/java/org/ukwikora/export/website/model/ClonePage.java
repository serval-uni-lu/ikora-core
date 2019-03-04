package org.ukwikora.export.website.model;

import org.ukwikora.analytics.CloneDetection;
import org.ukwikora.analytics.Clones;
import org.ukwikora.model.Project;
import org.ukwikora.model.UserKeyword;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

public class ClonePage {
    private final String id;
    private final String name;
    private final Table table;

    public ClonePage(String id, String name, List<Project> projects) {
        this.id = id;
        this.name = name;

        this.table = new Table(
                "clones",
                "Duplicated Code",
                new String[]{"Group", "Type", "Name", "File", "Lines", "Project"}
        );

        Clones<UserKeyword> UserKewordClones = CloneDetection.findClones(new HashSet<>(projects), UserKeyword.class);
    }
}
