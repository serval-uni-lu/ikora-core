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
package lu.uni.serval.ikora.core.builder;

import lu.uni.serval.ikora.core.BuildConfiguration;
import lu.uni.serval.ikora.core.error.Errors;
import lu.uni.serval.ikora.core.error.SymbolError;
import lu.uni.serval.ikora.core.model.Source;
import lu.uni.serval.ikora.core.utils.FileUtils;
import org.junit.jupiter.api.Assertions;
import lu.uni.serval.ikora.core.Helpers;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class BuildResultTest {
    @Test
    void testBuildSimpleProject(){
        BuildResult build = build("projects/web-demo");
        assertNotNull(build);

        Assertions.assertEquals(1, build.getProjects().size());

        assertNotNull(build.getSourceFile(getFileUri("projects/web-demo/gherkin_login.robot")));
        assertNotNull(build.getSourceFile(getFileUri("projects/web-demo/resource.robot")));
        assertNull(build.getSourceFile(URI.create("Fake/path/to/file.robot")));

        assertTrue(build.getBuildTime() >= 0);
        assertTrue(build.getResolveTime() >= 0);
        assertTrue(build.getParsingTime() >= 0);

        assertTrue(build.getErrors().isEmpty());
    }

    @Test
    void testBuildProjectWithErrors(){
        BuildResult build = build("projects/connected-projects");

        assertNotNull(build);
        assertEquals(1, build.getProjects().size());
        assertNotNull(build.getSourceFile(getFileUri("projects/connected-projects/project-a/test-cases.robot")));
        assertFalse(build.getErrors().isEmpty());

        Source source = build.getSourceFile(getFileUri("projects/connected-projects/project-a/test-cases.robot")).getSource();
        Errors errors = build.getErrors().in(source);

        Set<SymbolError> symbolErrors = errors.getSymbolErrors();
        assertEquals(1, symbolErrors.size());
    }

    @Test
    void testProjectsWithDependencies() throws IOException, URISyntaxException {
        final File projectAFile = FileUtils.getResourceFile("projects/connected-projects/project-a");
        assertNotNull(projectAFile);

        final File projectBFile = FileUtils.getResourceFile("projects/connected-projects/project-b");
        assertNotNull(projectBFile);

        final File projectCFile = FileUtils.getResourceFile("projects/connected-projects/project-c");
        assertNotNull(projectCFile);

        Set<File> files = new HashSet<>(Arrays.asList(projectAFile, projectBFile, projectCFile));
        final BuildResult build = Builder.build(files, Helpers.getConfiguration(), true);

        assertTrue(build.getBuildTime() >= 0);
        assertTrue(build.getResolveTime() >= 0);
        assertTrue(build.getParsingTime() >= 0);
        assertTrue(build.getDependencyResolutionTime() >= 0);
    }

    private static BuildResult build(String root) {
        try{
            File rootFolder = FileUtils.getResourceFile(root);
            return Builder.build(rootFolder, new BuildConfiguration(), true);
        }
        catch (Exception e){
            fail(String.format("Failed to build project %s: %s", root, e.getMessage()));
        }

        return null;
    }

    private static URI getFileUri(String location){
        try{
            return FileUtils.getResourceFile(location).toURI();
        }
        catch (Exception e){
            fail(String.format("Failed to get uri for file %s: %s", location, e.getMessage()));
        }

        return null;
    }
}
