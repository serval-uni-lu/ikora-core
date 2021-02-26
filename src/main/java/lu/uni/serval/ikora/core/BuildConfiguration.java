package lu.uni.serval.ikora.core;

import java.util.Collections;
import java.util.List;

/**
 * Configuration file used for parsing Robot Framework files
 */
public class BuildConfiguration {
    private List<String> extensions = Collections.singletonList("robot");
    private List<String> ignorePath = Collections.emptyList();

    /**
     * Get file extensions to be analyzed when parsing a project.
     * The name of the extensions does not contain the leading dot e.g. robot and not .robot.
     * @return A list of file extensions to be considered
     */
    public List<String> getExtensions() {
        return extensions;
    }

    /**
     * Set file extensions to be analyzed when parsing a project.
     * The name of the extensions does not contain the leading dot e.g. robot and not .robot.
     * @param extensions A list of file extensions to be considered
     */
    public void setExtensions(List<String> extensions) {
        this.extensions = extensions;
    }

    /**
     * Get folders to ignore when parsing a project.
     * The path is relative to the root of the project.
     * @return A list of relative paths
     */
    public List<String> getIgnorePath() {
        return ignorePath;
    }

    /**
     * Set folders to ignore when parsing a project.
     * The path is relative to the root of the project.
     * @param ignorePath A list of relative paths
     */
    public void setIgnorePath(List<String> ignorePath) {
        this.ignorePath = ignorePath;
    }
}
