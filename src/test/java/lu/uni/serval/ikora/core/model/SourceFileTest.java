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

import lu.uni.serval.ikora.core.builder.BuildResult;
import lu.uni.serval.ikora.core.builder.Builder;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class SourceFileTest {
    @Test
    void testBuiltinImport(){
        final String code =
                "*** Test Cases ***\n" +
                "Test with BuiltIn call\n" +
                "\tLog\tHello there!\n";

        final BuildResult build = Builder.build(code, true);
        final Project project = build.getProjects().iterator().next();

        final Optional<SourceFile> sourceFile = project.getSourceFile("<IN_MEMORY>");
        assertTrue(sourceFile.isPresent());

        assertTrue(sourceFile.get().isImportLibrary(Library.BUILTIN));
    }

    @Test
    void testLibraryImport(){
        final String code =
                "*** Settings ***\n" +
                "Library    Selenium2Library\n" +
                "*** Test Cases ***\n" +
                "Test with BuiltIn call\n" +
                "    Title Should Be    Login Page\n";

        final BuildResult build = Builder.build(code, true);
        final Project project = build.getProjects().iterator().next();

        final Optional<SourceFile> sourceFile = project.getSourceFile("<IN_MEMORY>");
        assertTrue(sourceFile.isPresent());

        final TestCase test = project.getTestCases().iterator().next();

        final Optional<KeywordCall> keywordCall = test.getSteps().get(0).getKeywordCall();
        assertTrue(keywordCall.isPresent());

        final Optional<Keyword> keyword = keywordCall.get().getKeyword();
        assertTrue(keyword.isPresent());

        assertTrue(sourceFile.get().isImportLibrary(keyword.get().getLibraryName()));
    }

    @Test
    void testMissingLibraryImport(){
        final String code =
            "*** Test Cases ***\n" +
            "Test with BuiltIn call\n" +
            "    Title Should Be    Login Page\n";

        final BuildResult build = Builder.build(code, true);
        final Project project = build.getProjects().iterator().next();

        final Optional<SourceFile> sourceFile = project.getSourceFile("<IN_MEMORY>");
        assertTrue(sourceFile.isPresent());

        assertFalse(sourceFile.get().isImportLibrary("Selenium2Library"));
    }

}
