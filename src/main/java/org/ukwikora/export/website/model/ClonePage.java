package org.ukwikora.export.website.model;

import org.ukwikora.analytics.Clone;
import org.ukwikora.analytics.CloneDetection;
import org.ukwikora.model.Project;
import org.ukwikora.model.UserKeyword;

import java.util.HashSet;
import java.util.List;

public class ClonePage extends Page {
    private final Table table;

    public ClonePage(String id, String name, List<Project> projects) throws Exception {
        super(id, name);

        this.table = new Table(
                "clones",
                "Duplicated Code",
                new String[]{"Group", "Type", "Name", "File", "Lines", "Project"}
        );

        for(Clone<UserKeyword> clone: CloneDetection.findClones(new HashSet<>(projects), UserKeyword.class)){
            this.table.addRow(new String[]{
                    "n/a",
                    clone.getType().name(),
                    clone.getStatement().getName(),
                    clone.getStatement().getFileName(),
                    clone.getStatement().getLineRange().toString(),
                    clone.getStatement().getFile().getProject().getName()
            });
        }
    }
}
