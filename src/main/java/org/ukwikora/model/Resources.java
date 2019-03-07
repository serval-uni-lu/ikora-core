package org.ukwikora.model;

import java.io.File;
import java.util.List;

public class Resources {
    private String name;
    private List<String> arguments;
    private String comment;
    private File file;

    private TestCaseFile testCasefile;

    public Resources(String name, File file, List<String> arguments, String comment) {
        this.name = name;
        this.arguments = arguments;
        this.comment = comment;
        this.file = file;
    }

    public String getName() {
        return this.name;
    }

    public File getFile() {
        return file;
    }

    public TestCaseFile getTestCaseFile() {
        return this.testCasefile;
    }

    public void setTestCaseFile(TestCaseFile testCasefile) {
        this.testCasefile = testCasefile;
    }

    public boolean isValid() {
        return file != null && testCasefile != null;
    }
}
