package lu.uni.serval.robotframework.compiler;

import lu.uni.serval.robotframework.model.LibraryKeyword;
import lu.uni.serval.robotframework.model.Project;
import org.reflections.Reflections;

import java.util.Set;

public class Compiler {
    static public Project compile(String filePath) {
        Project project = null;
        try {
            loadLibraries();

            project = parse(filePath);
            link(project);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return project;
    }

    static void loadLibraries() {
        Reflections reflections = new Reflections("lu.uni.serval.robotframework.libraries");
        Set<Class<? extends LibraryKeyword>> classes = reflections.getSubTypesOf(LibraryKeyword.class);

        System.out.println(classes);
    }

    static Project parse(String filePath) {
        return ProjectParser.parse(filePath);
    }

    static private void link(Project project) throws Exception {
        KeywordLinker.link(project);
        VariableLinker.link(project);
    }
}
