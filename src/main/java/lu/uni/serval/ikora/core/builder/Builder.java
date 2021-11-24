package lu.uni.serval.ikora.core.builder;

/*-
 * #%L
 * Ikora Core
 * %%
 * Copyright (C) 2019 - 2021 University of Luxembourg
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License")
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import lu.uni.serval.ikora.core.exception.InvalidArgumentException;
import lu.uni.serval.ikora.core.BuildConfiguration;
import lu.uni.serval.ikora.core.error.ErrorManager;
import lu.uni.serval.ikora.core.model.*;
import lu.uni.serval.ikora.core.runner.Runtime;
import lu.uni.serval.ikora.core.runner.StaticScope;
import lu.uni.serval.ikora.core.utils.FileUtils;

import java.io.File;
import java.time.Duration;
import java.time.Instant;
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
     * @param resolve If set to truth, resolve symbols otherwise just parse the files
     * @return Results containing the graph, statistics about the build and eventual errors
     */
    public static BuildResult build(Set<File> files, BuildConfiguration configuration, boolean resolve){
        BuildResult result = new BuildResult();
        ErrorManager errors = new ErrorManager();

        Projects projects = new Projects();

        Instant start = Instant.now();

        DynamicImports dynamicImports = new DynamicImports();
        for(File file: files) {
            Project project = parse(file, configuration, dynamicImports, errors);
            projects.addProject(project);
        }

        long parsingTime = Duration.between(start, Instant.now()).toMillis();
        result.setParsingTime(parsingTime);

        Instant startDependencies = Instant.now();
        resolveDependencies(projects);
        result.setDependencyResolutionTime(Duration.between(startDependencies, Instant.now()).toMillis());

        Instant startResolving = Instant.now();

        for(Project project: projects) {
                Runtime runtime = new Runtime(project, new StaticScope(), errors);
                loadLibraries(runtime);
                resolve(runtime, resolve);
        }

        result.setResolveTime(Duration.between(startResolving, Instant.now()).toMillis());
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
        resolve(runtime, link);
        result.setResolveTime(Duration.between(startLinking, Instant.now()).toMillis());

        result.setBuildTime(Duration.between(start, Instant.now()).toMillis());

        result.setErrors(errors);
        result.setProjects(new Projects(project));

        return result;
    }

    /**
     * Build a project from a folder or from a single file.
     * Note that if a relative path to another file is present, it will be included in the analysis as well.
     * @param code String containing the code to be analazed
     * @param link If set to truth, resolve symbols otherwise just parse the files
     * @return Results containing the graph, statistics about the build and eventual errors
     */
    public static BuildResult build(String code, boolean link) {
        BuildResult result = new BuildResult();
        ErrorManager errors = new ErrorManager();

        Instant start = Instant.now();

        DynamicImports dynamicImports = new DynamicImports();
        Project project = parse(code, dynamicImports, errors);
        result.setParsingTime(Duration.between(start, Instant.now()).toMillis());

        Instant startLinking = Instant.now();
        Runtime runtime = new Runtime(project, new StaticScope(), errors);
        loadLibraries(runtime);
        resolve(runtime, link);
        result.setResolveTime(Duration.between(startLinking, Instant.now()).toMillis());

        result.setBuildTime(Duration.between(start, Instant.now()).toMillis());

        result.setErrors(errors);
        result.setProjects(new Projects(project));

        return result;
    }

    private static void resolveDependencies(Projects projects) {
        for(Project project: projects){
            if(project.getRootFolder().isInMemory()){
                continue;
            }

            Set<Resources> externals =  project.getExternalResources();

            for(Resources external: externals) {
                for(Project dependency: projects) {
                    File base = dependency.getRootFolder().asFile();

                    if(FileUtils.isSubDirectory(base, external.getFile())){
                        updateDependencies(project, dependency);
                        break;
                    }
                }
            }
        }
    }

    private static void updateDependencies(Project project, Project dependency) {
        project.addDependency(dependency);
    }

    private static void loadLibraries(Runtime runtime) {
        LibraryResources libraries = LibraryLoader.load(runtime.getErrors());
        runtime.setLibraries(libraries);
    }

    private static void resolve(Runtime runtime, boolean resolve) {
        if(!resolve){
            return;
        }

        SymbolResolver.resolve(runtime);
    }

    private static Project parse(File file, BuildConfiguration configuration, DynamicImports dynamicImports, ErrorManager errors) {
        try {
            return ProjectParser.parse(file, configuration, dynamicImports, errors);
        } catch (InvalidArgumentException e) {
            errors.registerUnhandledError(null, e.getMessage(), e);
            return null;
        }
    }

    private static Project parse(String string, DynamicImports dynamicImports, ErrorManager errors) {
            return ProjectParser.parse(string, dynamicImports, errors);
    }
}
