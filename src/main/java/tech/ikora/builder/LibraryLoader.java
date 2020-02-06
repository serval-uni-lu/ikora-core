package tech.ikora.builder;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import tech.ikora.error.ErrorManager;
import tech.ikora.model.LibraryInfo;
import tech.ikora.model.LibraryKeyword;
import tech.ikora.model.LibraryResources;
import tech.ikora.model.LibraryVariable;
import org.reflections.Reflections;

import java.io.*;
import java.util.List;
import java.util.Set;

public class LibraryLoader {
    private LibraryLoader() {}

    public static LibraryResources load(ErrorManager errors) {
        LibraryResources libraries = new LibraryResources();

        loadBuiltInLibrary(libraries, errors);
        loadExternalLibrariesInfo(libraries, errors);

        return libraries;
    }

    private static void loadBuiltInLibrary(LibraryResources libraries, ErrorManager errors){
        Reflections keywordReflections = new Reflections("tech.ikora.libraries.builtin.keywords");
        Reflections variableReflections = new Reflections("tech.ikora.libraries.builtin.variables");

        Set<Class<? extends LibraryKeyword>> builtInKeywords = keywordReflections.getSubTypesOf(LibraryKeyword.class);
        Set<Class<? extends LibraryVariable>> variableClasses = variableReflections.getSubTypesOf(LibraryVariable.class);


        for(Class<? extends LibraryKeyword> builtInKeyword: builtInKeywords) {
            libraries.loadKeyword(builtInKeyword, errors);
        }

        for(Class<? extends LibraryVariable> libraryClass: variableClasses){
            libraries.loadVariable(libraryClass, errors);
        }
    }

    private static void loadExternalLibrariesInfo(LibraryResources libraries, ErrorManager errors){
        try {
            InputStream in = LibraryLoader.class.getResourceAsStream("/libraries.json");

            ObjectMapper mapper = new ObjectMapper();
            List<LibraryInfo> libraryInfos = mapper.readValue(in, new TypeReference<List<LibraryInfo>>(){});
            libraries.addExternalLibraries(libraryInfos);
        } catch (IOException e) {
            errors.registerIOError(
                    new File("libraries.json"),
                    "Failed to load internal file libraries.json containing definitions for library keywords"
            );
        }
    }
}
