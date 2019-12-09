package org.ikora;

import java.util.List;

public class Configuration {
    private List<String> extensions;
    private List<String> ignorePath;

    public List<String> getExtensions() {
        return extensions;
    }

    public void setExtensions(List<String> extensions) {
        this.extensions = extensions;
    }

    public List<String> getIgnorePath() {
        return ignorePath;
    }

    public void setIgnorePath(List<String> ignorePath) {
        this.ignorePath = ignorePath;
    }
}
