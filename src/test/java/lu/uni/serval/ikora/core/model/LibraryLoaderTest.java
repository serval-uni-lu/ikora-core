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

import lu.uni.serval.ikora.core.builder.LibraryLoader;
import lu.uni.serval.ikora.core.error.ErrorManager;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.reflections.Reflections;

import java.lang.reflect.InvocationTargetException;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class LibraryLoaderTest {
    private static LibraryResources resources;

    @BeforeAll
    static void setup(){
        final ErrorManager errors = new ErrorManager();
        resources = LibraryLoader.load(errors);
        assertTrue(errors.isEmpty());
    }

    @Test
    void testRegisterKeywordsIsComplete() {
        final Reflections keywordReflections = new Reflections("lu.uni.serval.ikora.core.libraries.builtin.keywords");
        final Set<Class<? extends LibraryKeyword>> builtInKeywords = keywordReflections.getSubTypesOf(LibraryKeyword.class);

        for(Class<? extends LibraryKeyword> keywordClass: builtInKeywords){
            final Token name = Token.fromString(LibraryKeyword.toKeyword(keywordClass));
            if(!resources.findKeyword("", name).isPresent()){
                fail(String.format("BuiltIn Keyword not properly registered: %s", keywordClass.getCanonicalName()));
            }
        }
    }

    @Test
    void testRegisterVariableIsComplete() throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        final Reflections variableReflections = new Reflections("lu.uni.serval.ikora.core.libraries.builtin.variables");
        final Set<Class<? extends LibraryVariable>> variableClasses = variableReflections.getSubTypesOf(LibraryVariable.class);

        for(Class<? extends LibraryVariable> variableClass: variableClasses){
            final Token name = Token.fromString(variableClass.getConstructor().newInstance().getName());
            if(!resources.findVariable(name).isPresent()){
                fail(String.format("BuiltIn Variable not properly registered: %s", variableClass.getCanonicalName()));
            }
        }
    }

    @Test
    void testLibraryImports() {
        final Optional<Keyword> builtin = resources.findKeyword("", Token.fromString("Call Method"));
        assertTrue(builtin.isPresent());

        final Optional<Keyword> external = resources.findKeyword("Selenium2Library", Token.fromString("Page Should Not Contain"));
        assertTrue(external.isPresent());
    }
}
