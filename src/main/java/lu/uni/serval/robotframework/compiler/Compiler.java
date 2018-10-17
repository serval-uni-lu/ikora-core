package lu.uni.serval.robotframework.compiler;

import lu.uni.serval.robotframework.model.Project;
import org.apache.log4j.Logger;

import java.time.Duration;
import java.time.Instant;

public class Compiler {
    final static Logger logger = Logger.getLogger(Compiler.class);

    static public Project compile(String filePath) {
        Instant start = Instant.now();

        Project project = null;
        try {
            project = parse(filePath);
            loadLibraries(project);
            link(project);

        } catch (Exception e) {
            e.printStackTrace();
        }

        Instant finish = Instant.now();
        long timeElapsed = Duration.between(start, finish).toMillis();

        logger.info(String.format("Project compiled in %d ms", timeElapsed));

        return project;
    }

    static private void loadLibraries(Project project) {
        LibraryLoader.load(project);
    }

    static private Project parse(String filePath) {
        return ProjectParser.parse(filePath);
    }

    static private void link(Project project) throws Exception {
        KeywordLinker.link(project);
        VariableLinker.link(project);
    }
}
