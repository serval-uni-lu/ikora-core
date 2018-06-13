package lu.uni.serval.robotframework.model;

import org.apache.commons.lang3.NotImplementedException;

import java.util.ArrayList;
import java.util.List;

public class Settings {
    private ResourcesTable resourcesTable;
    private ResourcesTable libraryTable;
    private List<String> defaultTags;
    private String documentation;

    public Settings() {
        this.resourcesTable = new ResourcesTable();
        this.libraryTable = new ResourcesTable();
        this.defaultTags = new ArrayList<>();
    }

    public String getDocumentation() {
        return documentation;
    }

    public ResourcesTable getResources() {
        return resourcesTable;
    }

    public ResourcesTable getLibraries() {
        return libraryTable;
    }

    public List<String> getDefaultTags(){
        return defaultTags;
    }

    public void setDocumentation(String documentation){
        this.documentation = documentation;
    }

    public void addResources(Resources resources){
        if(resources.getType() == Resources.Type.Resource){
            resourcesTable.add(resources);
        }
        else if(resources.getType() == Resources.Type.Library){
            libraryTable.add(resources);
        }
        else{
            throw new NotImplementedException("Expected Library or Resources got " + resources.getType().name() + " instead");
        }

    }

    public void addDefaultTag(String defaultTag){
        defaultTags.add(defaultTag);
    }
}
