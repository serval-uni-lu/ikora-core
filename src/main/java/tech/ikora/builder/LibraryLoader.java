package tech.ikora.builder;

import tech.ikora.error.ErrorManager;
import tech.ikora.model.LibraryKeyword;
import tech.ikora.model.LibraryResources;
import tech.ikora.model.LibraryVariable;
import org.reflections.Reflections;

import java.util.Set;

public class LibraryLoader {
    private LibraryLoader() {}

    public static LibraryResources load(ErrorManager errors) {
        Reflections reflections = new Reflections("tech.ikora.libraries");
        Set<Class<? extends LibraryKeyword>> keywordClasses = reflections.getSubTypesOf(LibraryKeyword.class);
        Set<Class<? extends LibraryVariable>> variableClasses = reflections.getSubTypesOf(LibraryVariable.class);

        LibraryResources libraries = new LibraryResources();

        for(Class<? extends LibraryKeyword> libraryClass: keywordClasses) {
            libraries.loadClass(libraryClass, errors);
        }

        for(Class<? extends LibraryVariable> libraryClass: variableClasses){
            libraries.loadClass(libraryClass, errors);
        }

        return libraries;
    }
}
