package org.ukwikora.export.website.model;

import org.ukwikora.model.Project;
import org.ukwikora.model.UserKeyword;
import org.ukwikora.model.Variable;
import org.ukwikora.utils.StringUtils;

import java.util.List;

public class DeadCode {
    private final Table table;

    public DeadCode(List<Project> projects) throws Exception {
        String[] labels = {"Type", "Name", "File", "Line", "Project"};

        this.table = new Table(
                "dead-code-table",
                "Dead Code",
                labels
        );

        String userKeywordType = StringUtils.toBeautifulName(UserKeyword.class.getSimpleName());
        String variableType = StringUtils.toBeautifulName(Variable.class.getSimpleName());

        for(Project project: projects){
            String projectName = StringUtils.toBeautifulName(project.getName());

            for(UserKeyword userKeyword: project.getUserKeywords()){
                if(userKeyword.getDependencies().isEmpty()){
                    table.addRow(new String[]{
                            userKeywordType,
                            userKeyword.getName(),
                            userKeyword.getFileName(),
                            userKeyword.getLineRange().toString(),
                            projectName
                    });
                }
            }

            for(Variable variable: project.getVariables()){
                if(variable.getDependencies().isEmpty()){
                    table.addRow(new String[]{
                            variableType,
                            variable.getName(),
                            variable.getFileName(),
                            variable.getLineRange().toString(),
                            projectName
                    });
                }
            }
        }
    }

    public Table getTable() {
        return table;
    }
}
