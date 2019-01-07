package org.ukwikora.utils;

import org.apache.commons.lang3.ArrayUtils;
import java.io.File;
import java.io.IOException;

public class FileUtils {
    public static String[] getSubFolders(File folder) {
        if(!folder.exists() || !folder.isDirectory()){
            return ArrayUtils.EMPTY_STRING_ARRAY;
        }

        return folder.list( (File current, String name) -> new File(current, name).isDirectory() );
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
}
