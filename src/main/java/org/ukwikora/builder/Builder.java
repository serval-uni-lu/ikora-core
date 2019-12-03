package org.ukwikora.builder;

import org.ukwikora.error.Error;
import org.ukwikora.error.IOError;
import org.ukwikora.model.*;
import org.ukwikora.runner.Runtime;
import org.ukwikora.runner.StaticScope;
import org.ukwikora.utils.FileUtils;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Builder {
    private List<Error> errors;

    private long parsingTime;
    private long dependencyResolutionTime;
    private long linkingTime;
    private long buildTime;

    public Builder(){
        errors = new ArrayList<>();
    }

    public Set<Project> build(Set<File> files, boolean link){
        Set<Project> projects = new HashSet<>();

        Instant start = Instant.now();

        DynamicImports dynamicImports = new DynamicImports();
        for(File file: files) {
            Project project = parse(file, dynamicImports);
            projects.add(project);
        }

        parsingTime = Duration.between(start, Instant.now()).toMillis();

        Instant startDependencies = Instant.now();
        resolveDependencies(projects);
        dependencyResolutionTime = Duration.between(startDependencies, Instant.now()).toMillis();

        Instant startLinking = Instant.now();

        for(Project project: projects) {
                Runtime runtime;
                runtime = new Runtime(project, new StaticScope());
                loadLibraries(runtime);
                link(runtime, link);
        }

        linkingTime = Duration.between(startLinking, Instant.now()).toMillis();
        buildTime = Duration.between(start, Instant.now()).toMillis();

        return projects;
    }

    private void resolveDependencies(Set<Project> projects) {
        for(Project project: projects){
            Set<Resources> externals =  project.getExternalResources();

            for(Resources external: externals) {
                for(Project dependency: projects) {
                    File base = dependency.getRootFolder();

                    try {
                        if(FileUtils.isSubDirectory(base, external.getFile())){
                            updateDependencies(project, dependency, external);
                            break;
                        }
                    } catch (IOException e) {
                        String message = String.format("Failed to resolve dependency: %s", e.getMessage());
                        errors.add(new IOError(message, base));
                    }
                }
            }
        }
    }

    private void updateDependencies(Project project, Project dependency, Resources external) {
        project.addDependency(dependency);
        String name = dependency.generateFileName(external.getFile());
        SourceFile sourceFile = dependency.getSourceFile(name);
        external.setSourceFile(sourceFile);
    }

    public Project build(File file, boolean link) {
        Project project;
        Instant start = Instant.now();

        DynamicImports dynamicImports = new DynamicImports();
        project = parse(file, dynamicImports);

        Runtime runtime = new Runtime(project, new StaticScope());
        loadLibraries(runtime);
        link(runtime, link);

        Instant finish = Instant.now();
        buildTime = Duration.between(start, finish).toMillis();

        return project;
    }

    private void loadLibraries(Runtime runtime) {
        LibraryResources libraries = LibraryLoader.load();
        runtime.setLibraries(libraries);
    }

    private void link(Runtime runtime, boolean link) {
        if(!link){
            return;
        }

        Linker.link(runtime, errors);
    }

    private Project parse(File file, DynamicImports dynamicImports) {
        return ProjectParser.parse(file, dynamicImports, errors);
    }
}
