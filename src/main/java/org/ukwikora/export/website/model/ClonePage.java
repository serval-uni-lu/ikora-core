package org.ukwikora.export.website.model;

import org.ukwikora.analytics.CloneCluster;
import org.ukwikora.analytics.CloneDetection;
import org.ukwikora.analytics.Clones;
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

        Clones<UserKeyword> clones = CloneDetection.findClones(new HashSet<>(projects), UserKeyword.class);

        int i = 0;
        for(CloneCluster<UserKeyword> cluster: clones.getClusters()){
            for(UserKeyword clone: cluster.getClones()){
                this.table.addRow(new String[]{
                        String.valueOf(i),
                        cluster.getType().name(),
                        clone.getName(),
                        clone.getFileName(),
                        clone.getLineRange().toString(),
                        clone.getFile().getProject().getName()
                });

            }
            ++i;
        }
    }

    public Table getTable() {
        return table;
    }
}
