/*
 *
 *     Copyright © 2019 - 2022 University of Luxembourg
 *
 *     Licensed under the Apache License, Version 2.0 (the "License")
 *     you may not use this file except in compliance with the License.
 *     You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *     Unless required by applicable law or agreed to in writing, software
 *     distributed under the License is distributed on an "AS IS" BASIS,
 *     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *     See the License for the specific language governing permissions and
 *     limitations under the License.
 */
package lu.uni.serval.ikora.core.analytics.violations;

import lu.uni.serval.ikora.core.model.SourceNode;

public class Violation {
    public enum Level{
        WARNING,
        ERROR
    }

    public enum Cause {
        MULTIPLE_DEFINITIONS,
        INFINITE_CALL_RECURSION,
        NO_DEFINITION_FOUND
    }

    private final Level level;
    private final SourceNode sourceNode;
    private final Cause cause;

    public Violation(Level level, SourceNode sourceNode, Cause cause) {
        this.level = level;
        this.sourceNode = sourceNode;
        this.cause = cause;
    }

    public Level getLevel() {
        return level;
    }

    public SourceNode getSourceNode() {
        return sourceNode;
    }

    public Cause getCause() {
        return cause;
    }
}
