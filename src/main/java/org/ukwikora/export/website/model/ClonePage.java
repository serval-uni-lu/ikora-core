package org.ukwikora.export.website.model;

import org.ukwikora.analytics.CloneCluster;
import org.ukwikora.analytics.Clones;
import org.ukwikora.model.UserKeyword;

public class ClonePage extends Page {
    private final Table table;

    public ClonePage(String id, String name, Clones<UserKeyword> clones) throws Exception {
        super(id, name);

        this.table = new Table(
                "clones",
                "Duplicated Code",
                new String[]{"Group", "Size", "Type", "Name", "File", "Lines", "Project"}
        );

        int i = 0;
        for(CloneCluster<UserKeyword> cluster: clones.getClusters()){
            int size = cluster.size();
            for(UserKeyword clone: cluster.getClones()){
                this.table.addRow(new String[]{
                        String.valueOf(i),
                        String.valueOf(size),
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

    public BarChart getChart() {
        return null;
    }
}
