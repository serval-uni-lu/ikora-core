package org.ukwikora.utils;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.tika.parser.txt.CharsetDetector;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Paths;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

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

    public static void copyResources(Class<?> caller, final String resources, final File destination) throws IOException, URISyntaxException {
        final File jarFile = new File(caller.getProtectionDomain().getCodeSource().getLocation().getPath());

        if(jarFile.isFile()) {
            final JarFile jar = new JarFile(jarFile);
            final Enumeration<JarEntry> entries = jar.entries();

            while(entries.hasMoreElements()) {
                final JarEntry entry = entries.nextElement();
                final String name = entry.getName();

                if (name.startsWith(resources + "/")) {
                    try(InputStream is = jar.getInputStream(entry)){
                        File destinationFile = new File(destination, StringUtils.removeStart(name, resources + "/"));
                        ensureExistFolder(destinationFile);

                        org.apache.commons.io.FileUtils.copyInputStreamToFile(is, destinationFile);
                    }
                }
            }
            jar.close();
        } else {
            File file = FileUtils.getResourceFile(resources);
            if(file.exists()){
                if(file.isDirectory()){
                    org.apache.commons.io.FileUtils.copyDirectory(file, destination);
                }
                else{
                    org.apache.commons.io.FileUtils.copyFile(file, destination);
                }
            }
        }
    }

    public static File getResourceFile(String name) throws IOException, URISyntaxException {
        URL resource = FileUtils.class.getClassLoader().getResource(name);
        if (resource == null) {
            throw new IOException("failed to locate resource template for project analytics");
        } else {
            return Paths.get(resource.toURI()).toFile();
        }
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

    public static void ensureExistFolder(File file) throws IOException {
        if(file.isFile()){
            file = file.getParentFile();
        }

        if(!file.exists()){
            if(!file.mkdirs()){
                throw new IOException(String.format("Failed to create folder '%s'", file.getAbsolutePath()));
            }
        }
    }
}
