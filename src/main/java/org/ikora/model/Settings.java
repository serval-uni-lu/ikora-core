package org.ikora.model;

import org.ikora.utils.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Settings implements Delayable {
    private List<Resources> resourcesTable;
    private List<Resources> externalResourcesTable;
    private List<Library> libraryTable;
    private List<String> defaultTags;
    private String documentation;
    private SourceFile file;
    private TimeOut timeOut;

    public Settings() {
        this.resourcesTable = new ArrayList<>();
        this.externalResourcesTable = new ArrayList<>();
        this.libraryTable = new ArrayList<>();
        this.defaultTags = new ArrayList<>();
        this.timeOut = TimeOut.none();
    }

    public SourceFile getFile() {
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

    public List<Resources> getAllResources() {
        return Stream.concat(resourcesTable.stream(), externalResourcesTable.stream())
                .collect(Collectors.toList());
    }

    public List<Library> getLibraries() {
        return libraryTable;
    }

    public List<String> getDefaultTags(){
        return defaultTags;
    }

    public void setFile(SourceFile file) {
        this.file = file;
    }

    public void setDocumentation(String documentation){
        this.documentation = documentation;
    }

    public void addResources(Resources resources) throws IOException {
        if(this.file == null){
            resourcesTable.add(resources);
        }
        else{
            Project project = this.file.getProject();
            File rootFolder = project.getRootFolder();

            if(FileUtils.isSubDirectory(rootFolder, resources.getFile())){
                resourcesTable.add(resources);
            }
            else{
                externalResourcesTable.add(resources);
            }
        }
    }

    public void addLibrary(Library library) {
        libraryTable.add(library);
    }

    public void addDefaultTag(String defaultTag){
        defaultTags.add(defaultTag);
    }

    @Override
    public TimeOut getTimeOut() {
        return timeOut;
    }

    @Override
    public void setTimeOut(TimeOut timeOut) {
        this.timeOut = timeOut;
    }
}
