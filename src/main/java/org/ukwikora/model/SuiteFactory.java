package org.ukwikora.model;

import org.apache.commons.io.FilenameUtils;
import org.ukwikora.utils.StringUtils;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

class SuiteFactory {
    static Suite create(TestCaseFile testCaseFile){
        return create(null, testCaseFile);
    }

    static Suite create(Suite parent, TestCaseFile testCaseFile){
        String name = computeName(parent, testCaseFile, true);
        File source = computeSource(parent, testCaseFile);

        Suite suite = new Suite(name, source);
        suite.addTestCaseFile(testCaseFile);

        return suite;
    }

    public static String computeName(TestCaseFile testCaseFile, boolean beautiful){
        return computeName(null, testCaseFile, beautiful);
    }

    static String computeName(Suite parent, TestCaseFile testCaseFile, boolean beautiful){
        String name = testCaseFile.getName();

        if(parent != null){
            Path base = Paths.get(parent.getSource().getAbsolutePath().trim());
            Path path = Paths.get(testCaseFile.getFile().getAbsolutePath().trim()).normalize();

            name = base.relativize(path).toString();
        }

        name =  Paths.get(name).getName(0).toString();

        if(beautiful){
            name = FilenameUtils.removeExtension(name);
            name = StringUtils.toBeautifulName(name);
        }

        return name;
    }

    private static File computeSource(Suite parent, TestCaseFile testCaseFile){
        File base;

        if(parent != null){
            base = parent.getSource();
        }
        else {
            base = testCaseFile.getProject().getRootFolder();

            if(base.isFile()){
                base = base.getParentFile();
            }
        }

        return new File(base, computeName(parent, testCaseFile, false));
    }
}
