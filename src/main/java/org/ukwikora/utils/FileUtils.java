package org.ukwikora.utils;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.tika.parser.txt.CharsetDetector;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Paths;

public class FileUtils {
    private FileUtils() {}

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

    public static Charset detectCharset(File f, Charset defaultCharset) {

        Charset charset = null;

        for (Charset current : Charset.availableCharsets().values()) {
            charset = validateCharset(f, current);
            if (charset != null) {
                break;
            }
        }

        if(charset == null){
            return defaultCharset;
        }

        return charset;
    }

    private static Charset validateCharset(File f, Charset defaultValue) {
        try(BufferedInputStream input = new BufferedInputStream(new FileInputStream(f))) {
            CharsetDetector detector = new CharsetDetector();
            detector.setText(input);

            String match = detector.detect().getName();
            return Charset.forName(match);
        } catch (Exception e) {
            return defaultValue;
        }
    }
}
