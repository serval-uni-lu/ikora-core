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
import lu.uni.serval.ikora.core.model.TestProcessing;
import lu.uni.serval.ikora.core.runner.Runtime;
import lu.uni.serval.ikora.core.runner.exception.RunnerException;

import java.util.Optional;

public class TestCaseExecutor extends NodeExecutor {
    private final TestCase testCase;

    public TestCaseExecutor(Runtime runtime, TestCase testCase) {
        super(runtime, testCase);
        this.testCase = testCase;
    }

    @Override
    public void executeImpl() throws RunnerException{
        try{
            final Optional<TestProcessing> setup = testCase.getSetup();
            if(setup.isPresent()){
                new TestProcessingExecutor(runtime, setup.get()).execute();
            }

            for(Step step: testCase.getSteps()){
                new StepExecutor(runtime, step).execute();
            }
        }
        finally {
            final Optional<TestProcessing> tearDown = testCase.getTearDown();
            if(tearDown.isPresent()){
                new TestProcessingExecutor(runtime, tearDown.get()).execute();
            }
        }
    }
}
