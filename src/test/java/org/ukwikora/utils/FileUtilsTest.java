package org.ukwikora.utils;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.ukwikora.Globals;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

import static org.junit.jupiter.api.Assertions.*;

public class FileUtilsTest {
    private static File base;
    private static File child1;
    private static File child2;

    @BeforeAll
    public static void initializeFiles() {
        base = new File("/test/root/");
        child1 = new File("/test/root/testCases.robot");
        child2 = new File("/test/root/subfolder1/testCases.robot");
    }

    @Test
    public void checkIsSubfolderDetectsWhenTrue() {
        try {
            assertTrue(FileUtils.isSubDirectory(base, base));
            assertTrue(FileUtils.isSubDirectory(base, child1));
            assertTrue(FileUtils.isSubDirectory(base, child2));
        } catch (IOException e) {
            fail("Io exception raised: " + e.getMessage());
        }
    }

    @Test
    public void checkIsSubfolderDetectsWhenFalse() {
        try {
            assertFalse(FileUtils.isSubDirectory(child1, base));
            assertFalse(FileUtils.isSubDirectory(child2, base));
        } catch (IOException e) {
            fail("Io exception raised: " + e.getMessage());
        }
    }

    @Test
    public void checkDetectCharsetWithUTF8(){
        File utf8 = Globals.getResourceFile("files/file-in-utf8.txt");
        Charset charset = FileUtils.detectCharset(utf8, null);
        assertNotNull(charset);
        assertEquals(Charset.forName("UTF-8"), charset);
    }

    @Test
    public void checkDetectCharsetWithISO88591(){
        File utf8 = Globals.getResourceFile("files/file-in-ISO-8859-1.txt");
        Charset charset = FileUtils.detectCharset(utf8, null);
        assertNotNull(charset);
        assertEquals(Charset.forName("ISO-8859-1"), charset);
    }

    @Test
    public void checkDetectCharsetWithUTF8BOM(){
        File utf8 = Globals.getResourceFile("files/file-in-utf8-bom.txt");
        Charset charset = FileUtils.detectCharset(utf8, null);
        assertNotNull(charset);
        assertEquals(Charset.forName("UTF8"), charset);
    }
}
