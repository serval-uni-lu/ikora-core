package lu.uni.serval.robotframework.compiler;

import lu.uni.serval.robotframework.model.LibraryKeyword;
import lu.uni.serval.robotframework.model.LibraryResources;
import lu.uni.serval.robotframework.model.Project;
import org.reflections.Reflections;

import java.util.Set;

public class Compiler {
    static public Project compile(String filePath) {
        Project project = null;
        try {
            LibraryResources libraries = loadLibraries();

            project = parse(filePath);
            project.setLibraries(libraries);

            link(project);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return project;
    }

    static LibraryResources loadLibraries() {
        Reflections reflections = new Reflections("lu.uni.serval.robotframework.libraries");
        Set<Class<? extends LibraryKeyword>> libraryClasses = reflections.getSubTypesOf(LibraryKeyword.class);

        LibraryResources libraries = new LibraryResources();

        for(Class<? extends LibraryKeyword> libraryClass: libraryClasses) {
            libraries.loadClass(libraryClass);
        }

        return libraries;
    }

    static Project parse(String filePath) {
        return ProjectParser.parse(filePath);
    }

    static private void link(Project project) throws Exception {
        KeywordLinker.link(project);
        VariableLinker.link(project);
    }
}
