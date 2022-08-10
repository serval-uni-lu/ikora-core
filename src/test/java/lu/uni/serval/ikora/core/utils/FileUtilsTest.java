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

import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import lu.uni.serval.ikora.core.Helpers;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.net.URI;
import java.net.URISyntaxException;
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

    @ParameterizedTest
    @ValueSource(strings = {
            "file-in-utf8.txt",
            "file-in-ISO-8859-1.txt",
            "file-in-utf8-bom.txt"
    })
    void testDetectCharset(String fileName) throws IOException, URISyntaxException {
        final File utf8 = FileUtils.getResourceFile("files/" + fileName);
        final Reader unicodeReader = FileUtils.getUnicodeReader(utf8);
        final String text = IOUtils.toString(unicodeReader);

        assertEquals("Text with strange characters: éèàçù", text);
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
