package lu.uni.serval.ikora.core.utils;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import lu.uni.serval.ikora.core.Helpers;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class FileUtilsTest {
    private static File base;
    private static File child1;
    private static File child2;

    @BeforeAll
    static void initializeFiles() {
        base = new File("/test/root/");
        child1 = new File("/test/root/testCases.robot");
        child2 = new File("/test/root/subfolder1/testCases.robot");
    }

    @Test
    void testIsSubfolderDetectsWhenTrue() {
        assertTrue(FileUtils.isSubDirectory(base, base));
        assertTrue(FileUtils.isSubDirectory(base, child1));
        assertTrue(FileUtils.isSubDirectory(base, child2));
    }

    @Test
    void testIsSubfolderDetectsWhenFalse() {
        assertFalse(FileUtils.isSubDirectory(child1, base));
        assertFalse(FileUtils.isSubDirectory(child2, base));
    }

    @Test
    void testGetSubdirectoriesWithNonEmptyDirectoryByFile() throws IOException, URISyntaxException {
        final File folder = FileUtils.getResourceFile("files/subdirectories");
        final Set<File> subFolders = FileUtils.getSubFolders(folder);

        assertEquals(2, subFolders.size());
    }

    @Test
    void testGetSubdirectoriesWithNonEmptyDirectoryByName() throws IOException, URISyntaxException {
        final File folder = FileUtils.getResourceFile("files/subdirectories");
        final Set<File> subFolders = FileUtils.getSubFolders(folder.getAbsolutePath());

        assertEquals(2, subFolders.size());
    }

    @Test
    void testDetectCharsetWithUTF8() throws IOException, URISyntaxException {
        final File utf8 = FileUtils.getResourceFile("files/file-in-utf8.txt");
        final Charset charset = FileUtils.detectCharset(utf8, null);

        assertNotNull(charset);
        assertEquals(StandardCharsets.UTF_8, charset);
    }

    @Test
    void testDetectCharsetWithISO88591() throws IOException, URISyntaxException {
        final File utf8 = FileUtils.getResourceFile("files/file-in-ISO-8859-1.txt");
        final Charset charset = FileUtils.detectCharset(utf8, null);

        assertNotNull(charset);
        assertEquals(StandardCharsets.ISO_8859_1, charset);
    }

    @Test
    void testDetectCharsetWithUTF8BOM() throws IOException, URISyntaxException {
        final File utf8 = FileUtils.getResourceFile("files/file-in-utf8-bom.txt");
        final Charset charset = FileUtils.detectCharset(utf8, null);

        assertNotNull(charset);
        assertEquals(StandardCharsets.UTF_8, charset);
    }

    @Test
    void testCopyResourceFile(){
        final File destination = Helpers.getNewTmpFolder("with sépcial and space/ikora-copy-resources-file");
        final String resources = "robot/clones.robot";

        try {
            FileUtils.copyResources(getClass(), resources, destination);
        } catch (Exception e) {
            fail("exception was raised: " + e.getMessage());
        }

        assertTrue(destination.exists());
        assertTrue(new File(destination, "clones.robot").isFile());
        Helpers.deleteDirectory(destination.getParentFile());
    }

    @Test
    void testGetParentFromSubPathWithValidInput() throws IOException {
        final File absolutePath = new File("/home/user/some/project/file.txt");
        final String subPath = "some/project/file.txt";

        final File parentFromSubPath = FileUtils.getParentFromSubPath(absolutePath, subPath);

        assertEquals(new File("/home/user").getPath(), parentFromSubPath.getPath());
    }

    @Test
    void testCopyResourceFolder(){
        final File destination = Helpers.getNewTmpFolder("with sépcial and space/ikora-copy-resources-folder");
        final String resources = "robot";

        try {
            FileUtils.copyResources(getClass(), resources, destination);
        } catch (Exception e) {
            fail("exception was raised: " + e.getMessage());
        }

        assertTrue(destination.exists());
        assertTrue(destination.isDirectory());
        Helpers.deleteDirectory(destination.getParentFile());
    }

    @Test
    void testGetRelativeResourcePathWithFile(){
        final URI resourceBase = URI.create("D:/projects/ikora-core/target/test-classes");
        final URI resourceFile = URI.create("D:/projects/ikora-core/target/test-classes/robot");

        final String relativeResourcePath = FileUtils.getRelativeResourcePath(resourceBase, resourceFile);

        assertEquals("robot", relativeResourcePath);
    }


    @Test
    void testGetRelativeResourcePathWithJar(){
        final URI resourceBase = URI.create("file:/C:/Users/user/ikora-test/file:/D:/projects/ikora-core/target/ikora-core-0.0.1.jar!");
        final URI resourceFile = URI.create("jar:file:///D:/projects/ikora-core/target/ikora-core-0.0.1.jar!/robot");

        final String relativeResourcePath = FileUtils.getRelativeResourcePath(resourceBase, resourceFile);

        assertEquals("robot", relativeResourcePath);
    }

    @Test
    void testCopyResourceNotExistingRaiseException(){
        final File destination = Helpers.getNewTmpFolder("some-folder-" + Instant.now().toEpochMilli());

        try{
            FileUtils.copyResources(getClass(), "not-existing", destination);
            fail("Should have raised IOException");
        }catch (Exception e) {
            assertTrue(e.getMessage().contains("not-existing"));
        }

        assertFalse(destination.exists());
    }
}
