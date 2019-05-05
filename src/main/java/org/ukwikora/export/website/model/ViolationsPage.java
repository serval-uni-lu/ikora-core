package org.ukwikora.export.website.model;

import org.ukwikora.analytics.Violation;
import org.ukwikora.analytics.ViolationDetection;
import org.ukwikora.export.exception.InvalidNumberColumnException;
import org.ukwikora.model.Project;

import java.util.List;

public class ViolationsPage extends Page{
    private final Table table;

    public ViolationsPage(String id, String name, List<Project> projects) throws InvalidNumberColumnException {
        super(id, name);

        this.table = new Table(
          "violations",
          "Violations",
          new String[]{"Type", "Name", "File", "Lines", "Project", "Message"}
        );

        for(Violation violation: ViolationDetection.detect(projects)){
            this.table.addRow(new String[]{
                    violation.getLevel().name(),
                    violation.getStatement().getName(),
                    violation.getStatement().getFileName(),
                    violation.getStatement().getLineRange().toString(),
                    violation.getStatement().getFile().getProject().getName(),
                    violation.getCause().toString()
            });
        }
    }

    public Table getTable() {
        return table;
    }
}
