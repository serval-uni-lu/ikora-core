package org.ukwikora.model;

import org.ukwikora.utils.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Settings {
    private List<Resources> resourcesTable;
    private List<Resources> externalResourcesTable;
    private List<Library> libraryTable;
    private List<String> defaultTags;
    private String documentation;
    private TestCaseFile file;

    public Settings() {
        this.resourcesTable = new ArrayList<>();
        this.externalResourcesTable = new ArrayList<>();
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

    public List<Resources> getExternalResources() {
        return externalResourcesTable;
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

    public void addResources(Resources resources) {
        if(this.file == null){
            resourcesTable.add(resources);
        }
        else{
            Project project = this.file.getProject();
            File rootFolder = project.getRootFolder();

            try {
                if(FileUtils.isSubDirectory(rootFolder, resources.getFile())){
                    resourcesTable.add(resources);
                }
                else{
                    externalResourcesTable.add(resources);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void addLibrary(Library library) {
        libraryTable.add(library);
    }

    public void addDefaultTag(String defaultTag){
        defaultTags.add(defaultTag);
    }

}
