package org.ukwikora;

import org.ukwikora.compiler.Compiler;
import org.ukwikora.model.Project;

import java.io.File;
import java.net.URL;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

public class Globals {
    public static double epsilon = 0.0001;

    public static File getResourceFile(String name){
        File file;

        try {
            URL resource = Globals.class.getClassLoader().getResource(name);
            if(resource == null) throw new Exception("Null URI returned for resources!");
            file = Paths.get(resource.toURI()).toFile();
        } catch (Exception e) {
            fail(String.format("Failed to load resource '%s': %s", name, e.getMessage()));
            file = null;
        }

        return file;
    }

    public static Project compileProject(String resourcesPath) {
        File projectFolder = getResourceFile(resourcesPath);
        return Compiler.compile(projectFolder.getAbsolutePath());
    }
}
