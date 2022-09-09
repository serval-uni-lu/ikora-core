/*
 *
 *     Copyright © 2019 - 2022 University of Luxembourg
 *
 *     Licensed under the Apache License, Version 2.0 (the "License")
 *     you may not use this file except in compliance with the License.
 *     You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *     Unless required by applicable law or agreed to in writing, software
 *     distributed under the License is distributed on an "AS IS" BASIS,
 *     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *     See the License for the specific language governing permissions and
 *     limitations under the License.
 */
package lu.uni.serval.ikora.core.utils;

import com.ibm.icu.text.CharsetDetector;
import com.ibm.icu.text.CharsetMatch;
import org.apache.commons.io.input.BOMInputStream;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

    public static void copyResources(final Class<?> caller, final String resources, final File destination) throws IOException, URISyntaxException {
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

                if (childPath.startsWith(relativePath)) {
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
            base = parseResourceName(base);
            child = parseResourceName(child);
        }

        return base.relativize(child).getPath();
    }

    public static File getResourceFile(String name) throws IOException, URISyntaxException {
        URL resource = FileUtils.class.getClassLoader().getResource(name);
        if (resource == null) {
            throw new IOException("Failed to locate resource template for project analytics");
        }

        return Paths.get(resource.toURI()).toFile();
    }

    public static Reader getUnicodeReader(File f) throws IOException {
        final CharsetDetector detector = new CharsetDetector();
        final BufferedInputStream dataStream = new BufferedInputStream(new BOMInputStream(Files.newInputStream(f.toPath())));
        detector.setText(dataStream);

        final CharsetMatch match = detector.detect();
        return match.getReader();
    }

    private static List<Path> walk(URI uri, final String resources) throws IOException {
        if ("jar".equals(uri.getScheme())) {
            return safeWalkJar(resources, uri);
        } else {
            return walkFiles(Paths.get(uri));
        }
    }

    private static List<Path> safeWalkJar(String path, URI uri) throws IOException {
        synchronized (getLock(uri)) {
            try (FileSystem fs = getFileSystem(uri)) {
                return walkFiles(fs.getPath(path));
            }
        }
    }

    private static List<Path> walkFiles(Path path) throws IOException {
        try (Stream<Path> stream = Files.walk(path)){
            return stream.collect(Collectors.toList());
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

    private static URI parseResourceName(URI uri){
        String schemeSpecificPart = uri.getSchemeSpecificPart();
        return URI.create(
                schemeSpecificPart
                .substring(schemeSpecificPart.indexOf("!") + 1)
                .replaceAll("^/", "")
        );
    }

    private static FileSystem getFileSystem(URI uri) throws IOException {
        try {
            return FileSystems.getFileSystem(uri);
        } catch (FileSystemNotFoundException e) {
            return FileSystems.newFileSystem(uri, Collections.<String, String>emptyMap());
        }
    }
}
