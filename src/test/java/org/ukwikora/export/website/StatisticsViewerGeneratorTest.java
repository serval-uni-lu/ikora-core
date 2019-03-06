package org.ukwikora.export.website;

import org.junit.Test;
import org.ukwikora.Globals;
import org.ukwikora.model.Project;

import java.io.File;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

public class StatisticsViewerGeneratorTest {
    @Test
    public void checkCopyResource(){
        File destination = Globals.getNewTmpFolder("ukwikora-copy-resources-test");

        try {
            StatisticsViewerGenerator generator = new StatisticsViewerGenerator(Collections.emptyList(), destination);
            generator.copyResources();
        } catch (Exception e) {
            fail("exception was raised: " + e.getMessage());
        }

        assertTrue(destination.exists());
        Globals.deleteDirectory(destination);
    }

    @Test
    public void checkSiteGeneration(){
        try {
            String[] paths = {
                    "robot/project-suite/project-A",
                    "robot/project-suite/project-B",
                    "robot/project-suite/project-C"
            };

            List<Project> projects = Globals.compileProjects(paths);
            File destination = Globals.getNewTmpFolder("static-site");

            StatisticsViewerGenerator generator = new StatisticsViewerGenerator(projects, destination);
            generator.generate();
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }
}