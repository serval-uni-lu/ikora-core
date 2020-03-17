package tech.ikora.builder;

import tech.ikora.BuildConfiguration;
import tech.ikora.error.ErrorManager;
import tech.ikora.runner.Runtime;
import tech.ikora.runner.StaticScope;
import tech.ikora.utils.FileUtils;
import tech.ikora.model.LibraryResources;
import tech.ikora.model.Project;
import tech.ikora.model.Resources;
import tech.ikora.model.SourceFile;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 * Entry point to construct the call graph and AST of a Robot Framework project
 */
public class Builder {
    private Builder(){}

    /**
     * Build a project indicating specifically which files and folders to analyze.
     * Note that if a relative path to another file is present, it will be included in the analysis as well.
     * @param files List File or Folder containing the files to analyze
     * @param configuration Configuration file
     * @param link If set to truth, resolve symbols otherwise just parse the files
     * @return Results containing the graph, statistics about the build and eventual errors
     */
    public static BuildResult build(Set<File> files, BuildConfiguration configuration, boolean link){
        BuildResult result = new BuildResult();
        ErrorManager errors = new ErrorManager();

        Set<Project> projects = new HashSet<>();

        Instant start = Instant.now();

        DynamicImports dynamicImports = new DynamicImports();
        for(File file: files) {
            Project project = parse(file, configuration, dynamicImports, errors);
            projects.add(project);
        }

        long parsingTime = Duration.between(start, Instant.now()).toMillis();
        result.setParsingTime(parsingTime);

        Instant startDependencies = Instant.now();
        resolveDependencies(projects, errors);
        result.setDependencyResolutionTime(Duration.between(startDependencies, Instant.now()).toMillis());

        Instant startLinking = Instant.now();

        for(Project project: projects) {
                Runtime runtime = new Runtime(project, new StaticScope(), errors);
                loadLibraries(runtime);
                link(runtime, link);
        }

        result.setLinkingTime(Duration.between(startLinking, Instant.now()).toMillis());
        result.setBuildTime(Duration.between(start, Instant.now()).toMillis());

        result.setErrors(errors);
        result.setProjects(projects);

        return result;
    }

    /**
     * Build a project from a folder or from a single file.
     * Note that if a relative path to another file is present, it will be included in the analysis as well.
     * @param file File or Folder containing the files to analyze
     * @param configuration Configuration file
     * @param link If set to truth, resolve symbols otherwise just parse the files
     * @return Results containing the graph, statistics about the build and eventual errors
     */
    public static BuildResult build(File file, BuildConfiguration configuration, boolean link) {
        BuildResult result = new BuildResult();
        ErrorManager errors = new ErrorManager();

        Instant start = Instant.now();

        DynamicImports dynamicImports = new DynamicImports();
        Project project = parse(file, configuration, dynamicImports, errors);
        result.setParsingTime(Duration.between(start, Instant.now()).toMillis());

        Instant startLinking = Instant.now();
        Runtime runtime = new Runtime(project, new StaticScope(), errors);
        loadLibraries(runtime);
        link(runtime, link);
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
                            updateDependencies(project, dependency, external);
                            break;
                        }
                    } catch (IOException e) {
                        errors.registerIOError(
                                base,
                                String.format("Failed to resolve dependency: %s", e.getMessage())
                        );
                    }
                }
            }
        }
    }

    private static void updateDependencies(Project project, Project dependency, Resources external) {
        project.addDependency(dependency);
        String name = dependency.generateFileName(external.getFile());
        Optional<SourceFile> sourceFile = dependency.getSourceFile(name);
        sourceFile.ifPresent(external::setSourceFile);
    }

    private static void loadLibraries(Runtime runtime) {
        LibraryResources libraries = LibraryLoader.load(runtime.getErrors());
        runtime.setLibraries(libraries);
    }

    private static void link(Runtime runtime, boolean link) {
        if(!link){
            return;
        }

        Linker.link(runtime);
    }

    private static Project parse(File file, BuildConfiguration configuration, DynamicImports dynamicImports, ErrorManager errors) {
        return ProjectParser.parse(file, configuration, dynamicImports, errors);
    }
}
