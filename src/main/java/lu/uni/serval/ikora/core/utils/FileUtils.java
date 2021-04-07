package lu.uni.serval.ikora.core.utils;

import lu.uni.serval.ikora.core.utils.charset.CharsetDetector;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

public class FileUtils {
    public static final String IN_MEMORY = "<IN_MEMORY>";
    private static final ConcurrentMap<String, Object> locks = new ConcurrentHashMap<>();

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

    public static File getParentFromSubPath(File absolutePath, String subPath) throws IOException {
        File output = absolutePath;
        File current = new File(subPath);

        while (true){
            if(!output.getName().equals(current.getName())){
                throw new IOException(String.format("Cannot substract subpath because %s is not a subPath of %s",
                        current.getPath(),
                        output.getPath()));
            }

            output = output.getParentFile();
            current = current.getParentFile();
            if (current == null) break;
        }

        return output;
    }

    public static boolean isSubDirectory(File base, File child) {
        try {
            base = base.getCanonicalFile();
            child = child.getCanonicalFile();
        } catch (IOException e) {
            return false;
        }

        return isSubDirectory(base.toPath(), child.toPath());
    }

    public static boolean isSubDirectory(Path base, Path child){
        return child.startsWith(base);
    }

    public static void copyResources(final Class<?> caller, final String resources, final File destination) throws Exception {
        final URL baseUrl = caller.getClassLoader().getResource(resources);

        if(baseUrl == null){
            throw new IOException("Invalid resources provided for copy: " + resources);
        }

        final URI resourceBase = getParentFromSubPath(new File(baseUrl.getFile()), resources).toURI();
        final List<Path> pathList = walk(baseUrl.toURI(), resources);

        destination.mkdirs();
        org.apache.commons.io.FileUtils.cleanDirectory(destination);

        File currentDirectory = destination;

        for (int i = 0; i < pathList.size(); ++i) {
            final Path path = pathList.get(i);
            final String relativePath = getRelativeResourcePath(pathList.get(0).toUri(), path.toUri());

            if(i + 1 < pathList.size()){
                final String childPath = getRelativeResourcePath(pathList.get(0).toUri(), pathList.get(i + 1).toUri());

                if (childPath.endsWith(relativePath)) {
                    currentDirectory = new File(destination, relativePath);
                    currentDirectory.mkdirs();
                    continue;
                }
            }

            final String resourcePath = getRelativeResourcePath(resourceBase, path.toUri());
            copyResourcesFile(caller, resourcePath, new File(currentDirectory, path.getFileName().toString()));
        }
    }

    private static void copyResourcesFile(final Class<?> caller, final String resources, final File destination) throws IOException {
        System.out.println("copyResourcesFile - resources: " + resources);
        System.out.println("copyResourcesFile - destination: " + destination.getAbsolutePath());
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

    public static String getRelativeResourcePath(URI base, URI child){
        if ("jar".equals(child.getScheme())) {
            final String schemeSpecificPart = child.getSchemeSpecificPart();
            return child.getSchemeSpecificPart()
                    .substring(schemeSpecificPart.indexOf("!") + 1);
        } else {
            return base.relativize(child).getPath();
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

    private static List<Path> walk(URI uri, final String resources) throws Exception {
        if ("jar".equals(uri.getScheme())) {
            return safeWalkJar(resources, uri);
        } else {
            return Files.walk(Paths.get(uri)).collect(Collectors.toList());
        }
    }

    private static List<Path> safeWalkJar(String path, URI uri) throws Exception {
        synchronized (getLock(uri)) {
            try (FileSystem fs = getFileSystem(uri)) {
                return Files.walk(fs.getPath(path)).collect(Collectors.toList());
            }
        }
    }

    private static Object getLock(URI uri) {
        String fileName = parseFileName(uri);
        locks.computeIfAbsent(fileName, s -> new Object());

        return locks.get(fileName);
    }

    private static String parseFileName(URI uri) {
        String schemeSpecificPart = uri.getSchemeSpecificPart();
        return schemeSpecificPart.substring(0, schemeSpecificPart.indexOf("!"));
    }

    private static FileSystem getFileSystem(URI uri) throws IOException {
        try {
            return FileSystems.getFileSystem(uri);
        } catch (FileSystemNotFoundException e) {
            return FileSystems.newFileSystem(uri, Collections.<String, String>emptyMap());
        }
    }
}
