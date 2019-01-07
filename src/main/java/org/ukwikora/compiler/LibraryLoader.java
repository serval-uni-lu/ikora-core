package org.ukwikora.compiler;

import org.ukwikora.model.LibraryKeyword;
import org.ukwikora.model.LibraryResources;
import org.ukwikora.model.LibraryVariable;
import org.reflections.Reflections;

import java.util.Set;

public class LibraryLoader {
    private LibraryLoader() {}

    static public LibraryResources load() {
        Reflections reflections = new Reflections("org.ukwikora.libraries");
        Set<Class<? extends LibraryKeyword>> keywordClasses = reflections.getSubTypesOf(LibraryKeyword.class);
        Set<Class<? extends LibraryVariable>> variableClasses = reflections.getSubTypesOf(LibraryVariable.class);

        LibraryResources libraries = new LibraryResources();

        for(Class<? extends LibraryKeyword> libraryClass: keywordClasses) {
            libraries.loadClass(libraryClass);
        }

        for(Class<? extends LibraryVariable> libraryClass: variableClasses){
            libraries.loadClass(libraryClass);
        }

        return libraries;
    }
}
