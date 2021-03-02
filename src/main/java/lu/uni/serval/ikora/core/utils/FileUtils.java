package lu.uni.serval.ikora.core.utils;

import lu.uni.serval.ikora.core.utils.charset.CharsetDetector;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class FileUtils {
    public static final String IN_MEMORY = "<IN_MEMORY>";

    private FileUtils() {}

    public static Set<File> getSubFolders(File folder) {
        if(!folder.exists() || !folder.isDirectory()){
            return Collections.emptySet();
        }

        File[] files = folder.listFiles( (File current, String name) -> new File(current, name).isDirectory() );

        if(files == null) {
            return Collections.emptySet();
        }

        return new HashSet<>(Arrays.asList(files));
    }

    public static Set<File> getSubFolders(String location) {
        return getSubFolders(new File(location));
    }

    public static boolean isSubDirectory(File base, File child) {
        try {
            base = base.getCanonicalFile();
            child = child.getCanonicalFile();
        } catch (IOException e) {
            return false;
        }


        File parentFile = child;
        while (parentFile != null) {
            if (base.equals(parentFile)) {
                return true;
            }
            parentFile = parentFile.getParentFile();
        }
        return false;
    }

    public static void copyResources(Class<?> caller, final String resources, final File destination) throws IOException {
        try(final InputStream resourceAsStream = caller.getClassLoader().getResourceAsStream(resources)){
            if(resourceAsStream == null){
                throw new IOException(String.format("Failed to create stream for resources '%s' called from '%s' class loader",
                        resources,
                        caller.getCanonicalName())
                );
            }

            org.apache.commons.io.FileUtils.copyInputStreamToFile(resourceAsStream, destination);
        }
    }

    public static File getResourceFile(String name) throws IOException, URISyntaxException {
        URL resource = FileUtils.class.getClassLoader().getResource(name);
        if (resource == null) {
            throw new IOException("Failed to locate resource template for project analytics");
        }

        return Paths.get(resource.toURI()).toFile();
    }

    public static Charset detectCharset(File f, Charset defaultCharset) {
        try(BufferedInputStream input = new BufferedInputStream(new FileInputStream(f))) {
            CharsetDetector detector = new CharsetDetector();
            detector.setText(input);

            String match = detector.detect().getName();
            return Charset.forName(match);
        } catch (Exception e) {
            return defaultCharset;
        }
    }
}
