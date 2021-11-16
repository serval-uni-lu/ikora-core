package lu.uni.serval.ikora.core.builder;

/*-
 * #%L
 * Ikora Core
 * %%
 * Copyright (C) 2019 - 2021 University of Luxembourg
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License")
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lu.uni.serval.ikora.core.error.ErrorManager;
import lu.uni.serval.ikora.core.model.*;
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
        Reflections keywordReflections = new Reflections("lu.uni.serval.ikora.core.libraries.builtin.keywords");
        Reflections variableReflections = new Reflections("lu.uni.serval.ikora.core.libraries.builtin.variables");

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
                    new Source(new File("libraries.json")),
                    "Failed to load internal file libraries.json containing definitions for library keywords"
            );
        }
    }
}
