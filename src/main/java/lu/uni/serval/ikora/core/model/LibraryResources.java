package lu.uni.serval.ikora.core.model;

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

import lu.uni.serval.ikora.core.libraries.LibraryKeywordInfo;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class LibraryResources {
    private final Map<String, KeywordObject> builtInKeywords;
    private final List<LibraryVariable> builtInVariables;
    private final Map<String, Map<String, LibraryKeywordInfo>> externalKeywords;

    public LibraryResources() {
        this.builtInKeywords = new HashMap<>();
        this.builtInVariables = new ArrayList<>();
        this.externalKeywords = new HashMap<>();
    }

    public void registerVariable(LibraryVariable libraryVariable){
        builtInVariables.add(libraryVariable);
    }

    public void registerKeyword(Class<? extends LibraryKeyword> libraryKeyword){
        final String keyword = LibraryKeyword.toKeyword(libraryKeyword);
        builtInKeywords.put(keyword, new KeywordObject(libraryKeyword));
    }

    public Optional<Keyword> findKeyword(String library, Token nameToken) {
        library = library != null ? library.toLowerCase() : "";
        String name = nameToken.getText().toLowerCase();

        LibraryKeyword keyword = null;

        if(!library.isEmpty()){
            keyword = externalKeywords.getOrDefault(library, new HashMap<>()).get(name);
        }

        if(keyword == null){
            final KeywordObject object = builtInKeywords.get(name);

            if(object != null){
                try {
                    keyword = object.getKeyword();
                } catch (Exception e) {}
            }
        }

        return Optional.ofNullable(keyword);
    }

    public Optional<LibraryVariable> findVariable(String library, Token name) {
        for(LibraryVariable variable: builtInVariables){
            if(variable.matches(name)){
                return Optional.of(variable);
            }
        }

        return Optional.empty();
    }

    public void addExternalLibraries(List<LibraryInfo> librariesInfo) {
        for(LibraryInfo library: librariesInfo){
            for(LibraryKeywordInfo keyword: library.getKeywords()){
                keyword.setLibrary(library);
                this.externalKeywords.putIfAbsent(library.getName().toLowerCase(), new HashMap<>());
                this.externalKeywords.get(library.getName().toLowerCase()).put(keyword.getName(), keyword);
            }
        }
    }

    protected static class KeywordObject {
        private final Class<? extends LibraryKeyword> itemClass;
        private LibraryKeyword itemObject;

        KeywordObject(Class<? extends LibraryKeyword> libraryClass) {
            this.itemClass = libraryClass;
            this.itemObject = null;
        }

        public LibraryKeyword getKeyword() throws IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
            if(itemObject == null) {
                itemObject = itemClass.getConstructor().newInstance();
            }

            return itemObject;
        }
    }
}
