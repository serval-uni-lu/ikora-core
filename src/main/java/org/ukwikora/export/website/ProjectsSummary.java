package org.ukwikora.export.website;

import org.ukwikora.model.Project;
import org.ukwikora.utils.JsonUtils;

import java.util.ArrayList;
import java.util.List;

public class ProjectsSummary {
    private List<String> names;
    private List<Integer> lines;

    public ProjectsSummary(List<Project> projects){
        int size = projects.size();

        names = new ArrayList<>(size);
        lines = new ArrayList<>(size);

        for(int i = 0; i < projects.size(); ++i){
            names.set(i, projects.get(i).getName());
            lines.set(i, projects.get(i).getLoc());
        }
    }

    public List<String> getNames(){
        return names;
    }

    public List<Integer> getLines(){
        return lines;
    }

    public String getJsonNames(){
        return JsonUtils.convertToJsonArray(names);
    }

    public String getJsonLines(){
        return JsonUtils.convertToJsonArray(lines);
    }
}
