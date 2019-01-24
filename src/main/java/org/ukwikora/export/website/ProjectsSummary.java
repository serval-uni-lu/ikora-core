package org.ukwikora.export.website;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.ukwikora.model.Project;
import org.ukwikora.utils.JsonUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ProjectsSummary {
    private static Logger logger = LogManager.getLogger(ProjectsSummary.class);

    private List<String> names;
    private List<Integer> lines;

    public ProjectsSummary(List<Project> projects){
        int size = projects.size();

        names = new ArrayList<>(size);
        lines = new ArrayList<>(size);

        for(Project project: projects){
            names.add(project.getName());
            lines.add(project.getLoc());
        }
    }

    public List<String> getNames(){
        return names;
    }

    public List<Integer> getLines(){
        return lines;
    }

    public String getJsonNames(){
        try {
            return JsonUtils.convertToJsonArray(names);
        } catch (IOException e) {
            logger.error(String.format("Failed to get json names project summary: %s", e.getMessage()));
            return "[]";
        }
    }

    public String getJsonLines(){
        try {
            return JsonUtils.convertToJsonArray(lines);
        } catch (IOException e) {
            logger.error(String.format("Failed to get json lines for project summary: %s", e.getMessage()));
            return "[]";
        }
    }
}
