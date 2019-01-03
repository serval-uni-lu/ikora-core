package org.ukwikora.utils;

import org.apache.commons.lang3.ArrayUtils;
import java.io.File;

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
}
