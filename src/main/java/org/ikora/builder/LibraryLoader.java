package org.ikora.builder;

import org.ikora.error.ErrorFile;
import org.ikora.error.ErrorManager;
import org.ikora.model.LibraryKeyword;
import org.ikora.model.LibraryResources;
import org.ikora.model.LibraryVariable;
import org.reflections.Reflections;

import java.util.Set;

public class LibraryLoader {
    private LibraryLoader() {}

    public static LibraryResources load(ErrorManager errors) {
        Reflections reflections = new Reflections("org.ikora.libraries");
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
