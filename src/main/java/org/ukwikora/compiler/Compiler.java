package org.ukwikora.compiler;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.ukwikora.model.*;
import org.ukwikora.utils.FileUtils;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Compiler {
    private static final Logger logger = LogManager.getLogger(Compiler.class);

    public static List<Project> compile(String[] paths){
        List<Project> projects = new ArrayList<>();

        logger.info("Start compilation...");
        Instant start = Instant.now();

        DynamicImports dynamicImports = new DynamicImports();
        for(String folder: paths) {
            try {
                Project project = parse(folder, dynamicImports);

                if(project != null){
                    projects.add(project);
                }
            } catch (Exception e) {
                logger.info(String.format("Failed to parse project located at %s: %s", folder, e.getMessage()));
            }
        }

        long stopParsing = Duration.between(start, Instant.now()).toMillis();
        logger.info(String.format("Projects parsed in %d ms", stopParsing));

        Instant startDependencies = Instant.now();

        resolveDependencies(projects);

        long stopDependencies = Duration.between(startDependencies, Instant.now()).toMillis();
        logger.info(String.format("Dependencies resolved in %d ms", stopDependencies));

        Instant startLinking = Instant.now();

        for(Project project: projects) {
            try {
                StaticRuntime runtime = new StaticRuntime(project, dynamicImports);
                loadLibraries(runtime);
                link(runtime);
            } catch (Exception e) {
                logger.info(String.format("Failed to link project %s: %s", project.getName(), e.getMessage()));
            }
        }

        long stopLinking = Duration.between(startLinking, Instant.now()).toMillis();
        logger.info(String.format("Projects linked in %d ms", stopLinking));

        long total = Duration.between(start, Instant.now()).toMillis();
        logger.info(String.format("Projects compiled in %d ms", total));

        return projects;
    }

    private static void resolveDependencies(List<Project> projects) {
        for(Project project: projects){
            Set<Resources> externals =  project.getExternalResources();

            for(Resources external: externals) {
                for(Project dependency: projects) {
                    try {
                        File base = dependency.getRootFolder();
                        if(FileUtils.isSubDirectory(base, external.getFile())){
                            updateDependencies(project, dependency, external);
                            break;
                        }
                    } catch (IOException e) {
                        logger.error("File IO exception raised during dependencies resolution: " + e.getMessage());
                    }
                }
            }
        }
    }

    private static void updateDependencies(Project project, Project dependency, Resources external) {
        project.addDependency(dependency);
        String name = dependency.generateFileName(external.getFile());
        TestCaseFile testCaseFile = dependency.getTestCaseFile(name);
        external.setTestCaseFile(testCaseFile);
    }

    public static Project compile(String filePath) {
        logger.info("Start compilation...");

        Project project;
        try {
            Instant start = Instant.now();

            DynamicImports dynamicImports = new DynamicImports();
            project = parse(filePath, dynamicImports);

            StaticRuntime runtime = new StaticRuntime(project, dynamicImports);
            loadLibraries(runtime);
            link(runtime);

            Instant finish = Instant.now();
            long timeElapsed = Duration.between(start, finish).toMillis();

            logger.info(String.format("Project %s compiled in %d ms", project.getName(), timeElapsed));

        } catch (Exception e) {
            logger.error(String.format("Failed to compile project located at %s: %s", filePath, e.getMessage()));
            project = null;
        }

        return project;
    }

    private static void loadLibraries(StaticRuntime runtime) {
        LibraryResources libraries = LibraryLoader.load();
        runtime.setLibraries(libraries);
    }

    private static void link(StaticRuntime runtime) throws Exception {
        Linker.link(runtime);
    }

    private static Project parse(String filePath, DynamicImports dynamicImports) throws Exception {
        return ProjectParser.parse(filePath, dynamicImports);
    }
}
