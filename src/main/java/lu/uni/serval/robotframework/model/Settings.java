package lu.uni.serval.robotframework.model;

import java.util.ArrayList;
import java.util.List;

public class Settings {
    private List<Resources> resourcesTable;
    private List<Library> libraryTable;
    private List<String> defaultTags;
    private String documentation;
    private TestCaseFile file;

    public Settings() {
        this.resourcesTable = new ArrayList<>();
        this.libraryTable = new ArrayList<>();
        this.defaultTags = new ArrayList<>();
    }

    public TestCaseFile getFile() {
        return file;
    }

    public String getDocumentation() {
        return documentation;
    }

    public List<Resources> getResources() {
        return resourcesTable;
    }

    public List<Library> getLibraries() {
        return libraryTable;
    }

    public List<String> getDefaultTags(){
        return defaultTags;
    }

    public void setFile(TestCaseFile file) {
        this.file = file;
    }

    public void setDocumentation(String documentation){
        this.documentation = documentation;
    }

    public void addResources(Resources resources){
        resourcesTable.add(resources);
    }

    public void addLibrary(Library library) {
        libraryTable.add(library);
    }

    public void addDefaultTag(String defaultTag){
        defaultTags.add(defaultTag);
    }

}
