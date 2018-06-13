package lu.uni.serval.robotframework.model;

import java.util.ArrayList;
import java.util.List;

public class Settings {
    private ResourcesTable resourcesTable;
    private List<String> defaultTags;
    private String documentation;

    public Settings() {
        this.resourcesTable = new ResourcesTable();
        this.defaultTags = new ArrayList<>();
    }

    public String getDocumentation() {
        return documentation;
    }

    public ResourcesTable getResources() {
        return resourcesTable;
    }

    public List<String> getDefaultTags(){
        return defaultTags;
    }

    public void setDocumentation(String documentation){
        this.documentation = documentation;
    }

    public void addResources(Resources resources){
        resourcesTable.add(resources);
    }

    public void addDefaultTag(String defaultTag){
        defaultTags.add(defaultTag);
    }
}
