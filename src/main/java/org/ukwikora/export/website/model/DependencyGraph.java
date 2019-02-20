package org.ukwikora.export.website.model;

import org.ukwikora.model.Project;
import org.ukwikora.utils.JsonUtils;
import org.ukwikora.utils.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DependencyGraph {
    private final String id;
    private final String name;
    private final List<Dependency> dependencies;

    public DependencyGraph(String id, String name, List<Project> projects) {
        this.id = id;
        this.name = name;
        this.dependencies = new ArrayList<>();

        for(Project target: projects){
            for(Project source: target.getDependencies()){
                String sourceName = StringUtils.toBeautifulName(source.getName());
                String targetName = StringUtils.toBeautifulName(target.getName());
                dependencies.add(new Dependency(sourceName, targetName, Dependency.Type.UserProject));
            }
        }
    }

    public String getJsonDependencies() throws IOException {
        return JsonUtils.convertToJsonArray(dependencies);
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getUrl(){
        return String.format("js/%s.js", getId());
    }
}
