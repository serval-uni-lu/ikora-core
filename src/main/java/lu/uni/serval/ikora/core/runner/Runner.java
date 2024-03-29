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
package lu.uni.serval.ikora.core.runner;

import lu.uni.serval.ikora.core.builder.LibraryLoader;
import lu.uni.serval.ikora.core.error.ErrorManager;
import lu.uni.serval.ikora.core.model.LibraryResources;
import lu.uni.serval.ikora.core.model.Project;
import lu.uni.serval.ikora.core.model.Suite;
import lu.uni.serval.ikora.core.runner.exception.InternalException;
import lu.uni.serval.ikora.core.runner.exception.RunnerException;
import lu.uni.serval.ikora.core.runner.executors.SuiteExecutor;
import lu.uni.serval.ikora.core.runner.report.Report;

import java.util.Optional;

public class Runner {
    private final Project project;
    private final Runtime runtime;

    public Runner(Project project, ExecutionFilter executionFilter, OutputStrategy outputStrategy){
        this.project = project;
        ErrorManager errorManager = new ErrorManager();
        LibraryResources libraryResources = LibraryLoader.load(errorManager);
        this.runtime = new Runtime(new DynamicScope(libraryResources), errorManager, executionFilter, outputStrategy);
    }

    public Report execute() throws RunnerException {
        runtime.reset();

        for(Suite suite: project.getSuites()){
            new SuiteExecutor(runtime, suite).execute();
        }

        runtime.finish();

        final Optional<Report> report = runtime.getReport();

        if(report.isEmpty()){
            throw new InternalException("Failed to create report during execution");
        }

        return report.get();
    }
}
