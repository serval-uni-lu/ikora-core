package tech.ikora.builder;

import tech.ikora.Configuration;
import tech.ikora.Helpers;
import tech.ikora.error.Errors;
import tech.ikora.error.SymbolError;
import tech.ikora.utils.FileUtils;
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
        BuildResult build = build("robot/web-demo");
        assertNotNull(build);

        assertEquals(1, build.getProjects().size());

        assertNotNull(build.getSourceFile(getFileUri("robot/web-demo/gherkin_login.robot")));
        assertNotNull(build.getSourceFile(getFileUri("robot/web-demo/resource.robot")));
        assertNull(build.getSourceFile(URI.create("Fake/path/to/file.robot")));

        assertTrue(build.getBuildTime() >= 0);
        assertTrue(build.getLinkingTime() >= 0);
        assertTrue(build.getParsingTime() >= 0);

        assertTrue(build.getErrors().isEmpty());
    }

    @Test
    void testBuildProjectWithErrors(){
        BuildResult build = build("robot/connected-projects");

        assertNotNull(build);
        assertEquals(1, build.getProjects().size());
        assertNotNull(build.getSourceFile(getFileUri("robot/connected-projects/project-a/test-cases.robot")));
        assertFalse(build.getErrors().isEmpty());

        File file = build.getSourceFile(getFileUri("robot/connected-projects/project-a/test-cases.robot")).getFile();
        Errors errors = build.getErrors().in(file);

        Set<SymbolError> symbolErrors = errors.getSymbolErrors();
        assertEquals(1, symbolErrors.size());
    }

    @Test
    void testProjectsWithDependencies() throws IOException, URISyntaxException {
        final File projectAFile = FileUtils.getResourceFile("robot/connected-projects/project-a");
        assertNotNull(projectAFile);

        final File projectBFile = FileUtils.getResourceFile("robot/connected-projects/project-b");
        assertNotNull(projectBFile);

        final File projectCFile = FileUtils.getResourceFile("robot/connected-projects/project-c");
        assertNotNull(projectCFile);

        Set<File> files = new HashSet<>(Arrays.asList(projectAFile, projectBFile, projectCFile));
        final BuildResult build = Builder.build(files, Helpers.getConfiguration(), true);

        assertTrue(build.getBuildTime() >= 0);
        assertTrue(build.getLinkingTime() >= 0);
        assertTrue(build.getParsingTime() >= 0);
        assertTrue(build.getDependencyResolutionTime() >= 0);
    }

    private static BuildResult build(String root) {
        try{
            File rootFolder = FileUtils.getResourceFile(root);
            return Builder.build(rootFolder, new Configuration(), true);
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