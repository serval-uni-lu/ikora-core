package org.ikora.builder;

import org.ikora.Configuration;
import org.ikora.utils.FileUtils;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.net.URI;

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

        assertTrue(build.getBuildTime() > 0);
        assertTrue(build.getLinkingTime() > 0);
        assertTrue(build.getParsingTime() > 0);

        assertTrue(build.getErrors().isEmpty());
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