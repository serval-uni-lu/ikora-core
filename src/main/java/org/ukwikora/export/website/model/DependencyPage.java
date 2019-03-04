package org.ukwikora.export.website.model;

import org.ukwikora.model.Project;
import org.ukwikora.utils.JsonUtils;
import org.ukwikora.utils.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DependencyPage extends Page {
    private final List<Dependency> dependencies;

    public DependencyPage(String id, String name, List<Project> projects) {
        super(id, name);

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

    public String getUrl(){
        return String.format("js/%s.js", getId());
    }
}
