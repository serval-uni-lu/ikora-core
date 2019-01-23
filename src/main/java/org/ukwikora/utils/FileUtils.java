package org.ukwikora.utils;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.ArrayUtils;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;

public class FileUtils {
    public static String[] getSubFolders(File folder) {
        if(!folder.exists() || !folder.isDirectory()){
            return ArrayUtils.EMPTY_STRING_ARRAY;
        }

        File[] files = folder.listFiles( (File current, String name) -> new File(current, name).isDirectory() );

        if(files == null) {
            return ArrayUtils.EMPTY_STRING_ARRAY;
        }

        String[] paths = new String[files.length];

        for(int i = 0; i < files.length; ++i) {
            paths[i] = FilenameUtils.normalize(files[i].getAbsolutePath());
        }

        return paths;
    }

    public static String[] getSubFolders(String location) {
        return getSubFolders(new File(location));
    }

    public static boolean isSubDirectory(File base, File child) throws IOException {
        base = base.getCanonicalFile();
        child = child.getCanonicalFile();

        File parentFile = child;
        while (parentFile != null) {
            if (base.equals(parentFile)) {
                return true;
            }
            parentFile = parentFile.getParentFile();
        }
        return false;
    }

    public static File getResourceFile(String name) throws IOException, URISyntaxException {
        URL resource = FileUtils.class.getClassLoader().getResource(name);

        if(resource == null){
            throw new IOException("failed to locate resource template for project analytics");
        }

        return Paths.get(resource.toURI()).toFile();
    }
}
