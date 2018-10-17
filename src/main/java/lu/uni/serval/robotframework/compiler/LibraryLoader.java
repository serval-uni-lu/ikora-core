package lu.uni.serval.robotframework.compiler;

import lu.uni.serval.robotframework.model.LibraryKeyword;
import lu.uni.serval.robotframework.model.LibraryResources;
import lu.uni.serval.robotframework.model.LibraryVariable;
import lu.uni.serval.robotframework.model.Project;
import org.reflections.Reflections;

import java.util.Set;

public class LibraryLoader {
    private LibraryLoader() {}

    static public void load(Project project) {
        Reflections reflections = new Reflections("lu.uni.serval.robotframework.libraries");
        Set<Class<? extends LibraryKeyword>> keywordClasses = reflections.getSubTypesOf(LibraryKeyword.class);
        Set<Class<? extends LibraryVariable>> variableClasses = reflections.getSubTypesOf(LibraryVariable.class);

        LibraryResources libraries = new LibraryResources();

        for(Class<? extends LibraryKeyword> libraryClass: keywordClasses) {
            libraries.loadClass(libraryClass);
        }

        for(Class<? extends LibraryVariable> libraryClass: variableClasses){
            libraries.loadClass(libraryClass);
        }

        project.setLibraries(libraries);
    }
}
