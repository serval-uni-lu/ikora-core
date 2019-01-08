package org.ukwikora.compiler;

import org.ukwikora.model.LibraryResources;
import org.ukwikora.model.Project;
import org.apache.log4j.Logger;
import org.ukwikora.model.Resources;
import org.ukwikora.model.TestCaseFile;
import org.ukwikora.utils.FileUtils;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Compiler {
    private final static Logger logger = Logger.getLogger(Compiler.class);

    static public List<Project> compile(String[] paths){
        List<Project> projects = new ArrayList<>();

        logger.info("Start compilation...");
        Instant start = Instant.now();

        for(String folder: paths) {
            try {
                Project project = parse(folder);

                if(project != null){
                    projects.add(project);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        long stopParsing = Duration.between(start, Instant.now()).toMillis();
        logger.info(String.format("\t- Projects parsed in %d ms", stopParsing));

        Instant startDependencies = Instant.now();

        resolveDependencies(projects);

        long stopDependencies = Duration.between(startDependencies, Instant.now()).toMillis();
        logger.info(String.format("Dependencies resolved in %d ms", stopDependencies));

        Instant startLinking = Instant.now();

        for(Project project: projects) {
            try {
                StaticRuntime runtime = new StaticRuntime(project);
                loadLibraries(runtime);
                link(runtime);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        long stopLinking = Duration.between(startLinking, Instant.now()).toMillis();
        logger.info(String.format("\n- Projects linked in %d ms", stopLinking));

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

    static public Project compile(String filePath) {
        logger.info("Start compilation...");

        Instant start = Instant.now();

        Project project = null;
        try {
            project = parse(filePath);
            StaticRuntime runtime = new StaticRuntime(project);
            loadLibraries(runtime);
            link(runtime);

        } catch (Exception e) {
            e.printStackTrace();
        }

        Instant finish = Instant.now();
        long timeElapsed = Duration.between(start, finish).toMillis();

        logger.info(String.format("Project compiled in %d ms", timeElapsed));

        return project;
    }

    static private void loadLibraries(StaticRuntime runtime) {
        LibraryResources libraries = LibraryLoader.load();
        runtime.setLibraries(libraries);
    }

    private static void link(StaticRuntime runtime) throws Exception {
        Linker.link(runtime);
    }

    static private Project parse(String filePath) {
        return ProjectParser.parse(filePath);
    }
}
