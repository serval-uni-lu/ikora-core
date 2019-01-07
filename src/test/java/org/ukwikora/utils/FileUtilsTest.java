package org.ukwikora.utils;

import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.*;

public class FileUtilsTest {
    private static File base;
    private static File child1;
    private static File child2;

    @BeforeClass
    public static void initializeFiles() {
        base = new File("/test/root/");
        child1 = new File("/test/root/testCases.robot");
        child2 = new File("/test/root/subfolder1/testCases.robot");
    }

    @Test
    public void checkIsSubfolderDetectsWhenTrue() {
        try {
            assertTrue(FileUtils.isSubDirectory(base, child1));
            assertTrue(FileUtils.isSubDirectory(base, child2));
        } catch (IOException e) {
            fail("Io exception raised: " + e.getMessage());
        }
    }

    @Test
    public void checkIsSubfolderDetectsWhenFalse() {
        try {
            assertFalse(FileUtils.isSubDirectory(base, base));
            assertFalse(FileUtils.isSubDirectory(child1, base));
            assertFalse(FileUtils.isSubDirectory(child2, base));
        } catch (IOException e) {
            fail("Io exception raised: " + e.getMessage());
        }
    }
}
