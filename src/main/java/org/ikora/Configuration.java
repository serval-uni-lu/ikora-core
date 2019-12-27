package org.ikora;

import java.util.Collections;
import java.util.List;

public class Configuration {
    private List<String> extensions = Collections.singletonList("robot");
    private List<String> ignorePath = Collections.emptyList();

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
