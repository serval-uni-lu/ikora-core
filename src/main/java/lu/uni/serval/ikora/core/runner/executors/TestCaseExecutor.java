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

import lu.uni.serval.ikora.core.model.Step;
import lu.uni.serval.ikora.core.model.TestCase;
import lu.uni.serval.ikora.core.runner.Runtime;
import lu.uni.serval.ikora.core.runner.exception.RunnerException;

public class TestCaseExecutor extends BaseExecutor {
    public TestCaseExecutor(Runtime runtime) {
        super(runtime);
    }

    public void execute(TestCase testCase) {
        try{
            runtime.enterNode(testCase);

            testCase.getSetup().ifPresent(setup -> new TestProcessingExecutor(runtime).execute(setup));

            final StepExecutor executor = new StepExecutor(runtime);

            for(Step step: testCase.getSteps()){
                executor.execute(step);
            }
        }
        catch (RunnerException e){

        }
        finally {
            testCase.getTearDown().ifPresent(tearDown -> new TestProcessingExecutor(runtime).execute(tearDown));
            runtime.exitNode(testCase);
        }
    }
}
