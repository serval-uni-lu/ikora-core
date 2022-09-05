/*
 *
 *     Copyright © 2022 University of Luxembourg
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
package lu.uni.serval.ikora.core.runner.executors;

import lu.uni.serval.ikora.core.model.Node;
import lu.uni.serval.ikora.core.runner.Runtime;
import lu.uni.serval.ikora.core.runner.exception.RunnerException;

public abstract class NodeExecutor {
    protected final Runtime runtime;
    private final Node node;

    protected NodeExecutor(Runtime runtime, Node node) {
        this.runtime = runtime;
        this.node = node;
    }

    public void execute() throws RunnerException {
        try{
            runtime.enterNode(node);
            executeImpl();
        }
        finally {
            runtime.exitNode(node);
        }
    }

    protected abstract void executeImpl() throws RunnerException;
}
