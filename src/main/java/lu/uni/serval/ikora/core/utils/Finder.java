/*
 *
 *     Copyright © 2019 - 2022 University of Luxembourg
 *
 *     Licensed under the Apache License, Version 2.0 (the "License")
 *     you may not use this file except in compliance with the License.
 *     You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *     Unless required by applicable law or agreed to in writing, software
 *     distributed under the License is distributed on an "AS IS" BASIS,
 *     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *     See the License for the specific language governing permissions and
 *     limitations under the License.
 */
package lu.uni.serval.ikora.core.utils;

import lu.uni.serval.ikora.core.model.*;
import org.apache.commons.lang3.tuple.Pair;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class Finder {
    private Finder () {}

    public static Set<Keyword> findKeywords(LibraryResources libraryResources, KeywordCall call){
        return findKeywords(libraryResources, call.getDefinitionToken(), call.getSourceFile());
    }

    private static Set<Keyword> findKeywords(LibraryResources libraryResources, Token fullName, SourceFile sourceFile) {
        Set<Keyword> keywordsFound = findKeywords(libraryResources, fullName, sourceFile, false);

        if(keywordsFound.isEmpty()){
            keywordsFound = findKeywords(libraryResources, fullName, sourceFile, true);
        }

        return keywordsFound;
    }

    private static Set<Keyword> findKeywords(LibraryResources libraryResources, Token fullName, SourceFile sourceFile, boolean allowSplit) {
        String library = "";
        Token name = fullName;

        if(allowSplit){
            Pair<Token, Token> libraryAndName = fullName.splitLibrary();

            library = libraryAndName.getLeft().getText();
            name = libraryAndName.getRight();
        }

        final Set<Keyword> keywordsFound = new HashSet<>(sourceFile.findUserKeyword(library, name));

        if(keywordsFound.isEmpty()){
            findLibraryKeyword(libraryResources, sourceFile.getAllLibraries(), name).ifPresent(keywordsFound::add);
        }

        return keywordsFound;
    }

    public static Optional<Keyword> findLibraryKeyword(LibraryResources libraryResources, Set<Library> libraries, Token name) {
        final Optional<Keyword> libraryKeyword = libraries.stream()
                .map(Library::getName)
                .map(library -> libraryResources.findKeyword(library, name))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .findFirst();

        return libraryKeyword.isPresent() ? libraryKeyword : libraryResources.findKeyword("", name);
    }

    public static Optional<LibraryVariable> findLibraryVariable(LibraryResources libraryResources, Token name) {
        return libraryResources.findVariable(name);
    }
}
