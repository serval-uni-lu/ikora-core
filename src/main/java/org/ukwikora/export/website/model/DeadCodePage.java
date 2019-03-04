package org.ukwikora.export.website.model;

import org.ukwikora.model.Project;
import org.ukwikora.model.UserKeyword;
import org.ukwikora.model.Variable;
import org.ukwikora.utils.StringUtils;

import java.util.List;

public class DeadCodePage {
    private final String id;
    private final String name;
    private final Table table;

    public DeadCodePage(String id, String name, List<Project> projects) throws Exception {
        this.id = id;
        this.name = name;

        this.table = new Table(
                "dead-code-table",
                "Dead Code",
                new String[]{"Type", "Name", "File", "Lines", "Project"}
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

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Table getTable() {
        return table;
    }
}
