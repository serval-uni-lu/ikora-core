package lu.uni.serval.ikora.core.runner;

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

import lu.uni.serval.ikora.core.builder.LibraryLoader;
import lu.uni.serval.ikora.core.error.ErrorManager;
import lu.uni.serval.ikora.core.exception.RunnerException;
import lu.uni.serval.ikora.core.model.Project;
import lu.uni.serval.ikora.core.model.Suite;
import lu.uni.serval.ikora.core.report.Report;

import java.util.Optional;

public class Runner {
    private final Project project;
    private final Runtime runtime;

    Runner(Project project){
        this.project = project;

        this.runtime = new Runtime(this.project, new DynamicScope(), new ErrorManager());
        runtime.setLibraries(LibraryLoader.load(runtime.getErrors()));
    }

    public Report execute() throws Exception{
        runtime.reset();

        for(Suite suite: project.getSuites()){
            suite.execute(runtime);
        }

        runtime.finish();

        Optional<Report> report = runtime.getReport();

        if(!report.isPresent()){
            throw new RunnerException("Failed to create report during execution");
        }

        return report.get();
    }
}
