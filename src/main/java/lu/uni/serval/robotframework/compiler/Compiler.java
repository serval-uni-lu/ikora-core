package lu.uni.serval.robotframework.compiler;

import lu.uni.serval.robotframework.model.Project;

public class Compiler {
    static public Project compile(String filePath) {
        Project project = null;
        try {
            project = parse(filePath);
            loadLibraries(project);
            link(project);

        } catch (Exception e) {
            e.printStackTrace();
        }

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
