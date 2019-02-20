package org.ukwikora.export.website;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Collections;

import static org.junit.Assert.*;

public class StatisticsViewerGeneratorTest {
    @Test
    public void checkCopyResource(){
        String tmpPath = System.getProperty("java.io.tmpdir");
        File tmpDir = new File(tmpPath);
        File destination = new File(tmpDir, "ukwikora-copy-resources-test");
        deleteDirectory(destination);

        assertFalse(destination.exists());

        try {
            StatisticsViewerGenerator generator = new StatisticsViewerGenerator(Collections.emptyList(), destination);
            generator.copyResources();
        } catch (Exception e) {
            fail("exception was raised: " + e.getMessage());
        }

        assertTrue(destination.exists());
        deleteDirectory(destination);
    }

    private void deleteDirectory(File directory){
        if(directory.exists()){
            try {
                FileUtils.deleteDirectory(directory);
            } catch (IOException e) {
                fail("Failed to clean " + directory.getAbsolutePath());
            }
        }
    }
}