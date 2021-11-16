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
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;

import static org.junit.jupiter.api.Assertions.*;

class LibraryLoaderTest {
    @Test
    void testLibraryImports() throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        ErrorManager errors = new ErrorManager();
        LibraryResources resources = LibraryLoader.load(errors);
        assertTrue(errors.isEmpty());

        final Keyword builtin = resources.findKeyword(Token.fromString("Call Method"));
        assertNotNull(builtin);

        final Keyword external = resources.findKeyword(Token.fromString("Page Should Not Contain"));
        assertNotNull(external);
    }
}
