package tech.ikora;

import org.apache.commons.io.FileUtils;
import tech.ikora.builder.BuildResult;
import tech.ikora.builder.Builder;
import tech.ikora.builder.LineReader;
import tech.ikora.model.Project;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

public class Helpers {
    public static double epsilon = 0.0001;

    public static Project compileProject(String resourcesPath, boolean resolve) {
        File projectFolder = null;
        try {
            projectFolder = tech.ikora.utils.FileUtils.getResourceFile(resourcesPath);
        } catch (Exception e) {
            fail(String.format("Failed to load '%s': %s", resourcesPath, e.getMessage()));
        }

        final BuildResult result = Builder.build(projectFolder, getConfiguration(), resolve);
        assertEquals(1, result.getProjects().size());

        return result.getProjects().iterator().next();
    }

    public static File getNewTmpFolder(String name){
        String tmpPath = System.getProperty("java.io.tmpdir");
        File tmpDir = new File(tmpPath);
        File directory = new File(tmpDir, name);
        deleteDirectory(directory);

        assertFalse(directory.exists());

        return directory;
    }

    public static void deleteDirectory(File directory){
        if(directory.exists()){
            try {
                FileUtils.deleteDirectory(directory);
            } catch (IOException e) {
                fail("Failed to clean " + directory.getAbsolutePath());
            }
        }
    }

    public static BuildConfiguration getConfiguration(){
        final BuildConfiguration configuration = new BuildConfiguration();
        configuration.setExtensions(Collections.singletonList("robot"));
        configuration.setIgnorePath(Collections.emptyList());

        return configuration;
    }

    public static LineReader lineReader(String text) throws IOException {
        Reader targetReader = new StringReader(text);
        LineReader lineReader = new LineReader(targetReader);
        lineReader.readLine();

        return lineReader;
    }

    public static LineReader lineReader(String... tokens) throws IOException {
        String line = String.join("\t", tokens);
        return lineReader(line);
    }
}
