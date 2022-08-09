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
package lu.uni.serval.ikora.core.analytics.visitor;

import lu.uni.serval.ikora.core.Helpers;
import lu.uni.serval.ikora.core.model.Project;
import lu.uni.serval.ikora.core.model.SourceFile;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class SizeVisitorTest {
    private static Project project;

    @BeforeAll
    static void setup(){
        project = Helpers.compileProject("robot/web-demo", true);
    }

    @Test
    void testFileWithResources(){
        final Optional<SourceFile> sourceFile = project.getSourceFile("gherkin_login.robot");
        assertTrue(sourceFile.isPresent());

        final SizeVisitor sizeVisitor = new SizeVisitor();
        sizeVisitor.visit(sourceFile.get(), new PathMemory());

        assertEquals(1, sizeVisitor.getResult().getTestCaseSize());
        assertEquals(8, sizeVisitor.getResult().getUserKeywordSize());
    }

    @Test
    void testFileWithoutResources(){
        final Optional<SourceFile> sourceFile = project.getSourceFile("resource.robot");
        assertTrue(sourceFile.isPresent());

        final SizeVisitor sizeVisitor = new SizeVisitor();
        sizeVisitor.visit(sourceFile.get(), new PathMemory());

        assertEquals(0, sizeVisitor.getResult().getTestCaseSize());
        assertEquals(7, sizeVisitor.getResult().getUserKeywordSize());
    }
}
