package org.ukwikora;

import org.apache.commons.io.FileUtils;
import org.ukwikora.compiler.Compiler;
import org.ukwikora.model.Project;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class Helpers {
    public static double epsilon = 0.0001;

    public static Project compileProject(String resourcesPath) {
        File projectFolder = null;
        try {
            projectFolder = org.ukwikora.utils.FileUtils.getResourceFile(resourcesPath);
        } catch (Exception e) {
            fail(String.format("Failed to load '%s': %s", resourcesPath, e.getMessage()));
        }

        return Compiler.compile(projectFolder.getAbsolutePath());
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

    public static File getResourceFile(String resources) {
        try {
            return org.ukwikora.utils.FileUtils.getResourceFile(resources);
        } catch (Exception e){
            fail(String.format("Failed to load resources '%s': %s", resources, e.getMessage()));
        }

        return null;
    }
}
