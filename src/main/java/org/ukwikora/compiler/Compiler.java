package org.ukwikora.compiler;

import org.ukwikora.model.LibraryResources;
import org.ukwikora.model.Project;
import org.apache.log4j.Logger;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class Compiler {
    final static Logger logger = Logger.getLogger(Compiler.class);

    static public List<Project> compile(String[] paths){
        List<Project> projects = new ArrayList<>();

        for(String folder: paths) {
            Project project = Compiler.compile(folder);

            if(project != null){
                projects.add(project);
            }
        }

        for(Project project: projects) {
            project.resolveDependencies(projects);
        }

        return projects;
    }

    static public Project compile(String filePath) {
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
