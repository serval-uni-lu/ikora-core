package org.ikora.builder;

import org.ikora.error.ErrorManager;
import org.ikora.model.*;
import org.ikora.runner.Runtime;
import org.ikora.runner.StaticScope;
import org.ikora.utils.FileUtils;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class Builder {
    private Builder(){}

    public static BuildResult build(Set<File> files, boolean link){
        BuildResult result = new BuildResult();
        ErrorManager errors = new ErrorManager();

        Set<Project> projects = new HashSet<>();

        Instant start = Instant.now();

        DynamicImports dynamicImports = new DynamicImports();
        for(File file: files) {
            Project project = parse(file, dynamicImports, errors);
            projects.add(project);
        }

        long parsingTime = Duration.between(start, Instant.now()).toMillis();
        result.setParsingTime(parsingTime);

        Instant startDependencies = Instant.now();
        resolveDependencies(projects, errors);
        result.setDependencyResolutionTime(Duration.between(startDependencies, Instant.now()).toMillis());

        Instant startLinking = Instant.now();

        for(Project project: projects) {
                Runtime runtime;
                runtime = new Runtime(project, new StaticScope());
                loadLibraries(runtime);
                link(runtime, link, errors);
        }

        result.setLinkingTime(Duration.between(startLinking, Instant.now()).toMillis());
        result.setBuildTime(Duration.between(start, Instant.now()).toMillis());

        result.setErrors(errors);
        result.setProjects(projects);

        return result;
    }

    public static BuildResult build(File file, boolean link) {
        BuildResult result = new BuildResult();
        ErrorManager errors = new ErrorManager();

        Instant start = Instant.now();

        DynamicImports dynamicImports = new DynamicImports();
        Project project = parse(file, dynamicImports, errors);
        result.setParsingTime(Duration.between(start, Instant.now()).toMillis());

        Instant startLinking = Instant.now();
        Runtime runtime = new Runtime(project, new StaticScope());
        loadLibraries(runtime);
        link(runtime, link, errors);
        result.setLinkingTime(Duration.between(startLinking, Instant.now()).toMillis());

        result.setBuildTime(Duration.between(start, Instant.now()).toMillis());

        result.setErrors(errors);
        result.setProjects(Collections.singleton(project));

        return result;
    }

    private static void resolveDependencies(Set<Project> projects, ErrorManager errors) {
        for(Project project: projects){
            Set<Resources> externals =  project.getExternalResources();

            for(Resources external: externals) {
                for(Project dependency: projects) {
                    File base = dependency.getRootFolder();

                    try {
                        if(FileUtils.isSubDirectory(base, external.getFile())){
                            updateDependencies(project, dependency, external, errors);
                            break;
                        }
                    } catch (IOException e) {
                        errors.registerIOError(
                                String.format("Failed to resolve dependency: %s", e.getMessage()),
                                base
                        );
                    }
                }
            }
        }
    }

    private static void updateDependencies(Project project, Project dependency, Resources external, ErrorManager errors) {
        project.addDependency(dependency);
        String name = dependency.generateFileName(external.getFile());
        Optional<SourceFile> sourceFile = dependency.getSourceFile(name);

        if(sourceFile.isPresent()){
            external.setSourceFile(sourceFile.get());
        }
    }

    private static void loadLibraries(Runtime runtime) {
        LibraryResources libraries = LibraryLoader.load();
        runtime.setLibraries(libraries);
    }

    private static void link(Runtime runtime, boolean link, ErrorManager errors) {
        if(!link){
            return;
        }

        Linker.link(runtime, errors);
    }

    private static Project parse(File file, DynamicImports dynamicImports, ErrorManager errors) {
        return ProjectParser.parse(file, dynamicImports, errors);
    }
}
