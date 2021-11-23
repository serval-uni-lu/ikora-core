package lu.uni.serval.ikora.core;

/*-
 * #%L
 * Ikora Core
 * %%
 * Copyright (C) 2019 - 2021 University of Luxembourg
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License")
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

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
