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

import lu.uni.serval.ikora.core.model.Keyword;
import lu.uni.serval.ikora.core.model.KeywordCall;
import lu.uni.serval.ikora.core.model.Step;
import lu.uni.serval.ikora.core.runner.Runtime;
import lu.uni.serval.ikora.core.utils.Finder;
import lu.uni.serval.ikora.core.runner.exception.RunnerException;

import java.util.Set;

public class StepExecutor extends BaseExecutor{
    StepExecutor(Runtime runtime) {
        super(runtime);
    }

    void execute(Step step) throws RunnerException {
        if(step instanceof KeywordCall){
            execute((KeywordCall)step);
        }
    }

    private void execute(KeywordCall call) throws RunnerException {
        final Set<Keyword> keywords = Finder.findKeywords(runtime.getLibraryResources(), call);

        if(keywords.isEmpty()){
            runtime.registerSymbolErrorAndThrow(call.getSource(), "No definition found", call.getRange());
        }

        if(keywords.size() > 1){
            runtime.registerSymbolErrorAndThrow(call.getSource(), "Found multiple definition", call.getRange());
        }

        new KeywordExecutor(runtime).execute(keywords.iterator().next());
    }
}
