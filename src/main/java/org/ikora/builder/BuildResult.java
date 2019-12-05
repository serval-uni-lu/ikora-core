package org.ikora.builder;

import org.ikora.error.ErrorManager;
import org.ikora.model.Project;
import org.ikora.model.SourceFile;

import java.net.URI;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;

public class BuildResult {
    private ErrorManager errors;
    private Set<Project> projects;

    private long parsingTime;
    private long dependencyResolutionTime;
    private long linkingTime;
    private long buildTime;

    public BuildResult(){
        this.errors = new ErrorManager();
        this.projects = Collections.emptySet();

        parsingTime = -1;
        dependencyResolutionTime = -1;
        linkingTime = -1;
        buildTime = -1;
    }

    public ErrorManager getErrors() {
        return errors;
    }

    public Set<Project> getProjects() {
        return projects;
    }

    public void setErrors(ErrorManager errors) {
        this.errors = errors;
    }

    public long getParsingTime() {
        return parsingTime;
    }

    public void setParsingTime(long parsingTime) {
        this.parsingTime = parsingTime;
    }

    public long getDependencyResolutionTime() {
        return dependencyResolutionTime;
    }

    public void setDependencyResolutionTime(long dependencyResolutionTime) {
        this.dependencyResolutionTime = dependencyResolutionTime;
    }

    public long getLinkingTime() {
        return linkingTime;
    }

    public void setLinkingTime(long linkingTime) {
        this.linkingTime = linkingTime;
    }

    public long getBuildTime() {
        return buildTime;
    }

    public void setBuildTime(long buildTime) {
        this.buildTime = buildTime;
    }

    public void setProjects(Set<Project> projects) {
        this.projects = projects;
    }

    public SourceFile getSourceFile(URI uri){
        for(Project project: projects){
            Optional<SourceFile> optional = project.getSourceFile(uri);

            if(optional.isPresent()){
                return optional.get();
            }
        }

        return null;
    }
}
