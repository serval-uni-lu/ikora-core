package org.ukwikora.runner;

import org.ukwikora.model.Project;
import org.ukwikora.model.TestCase;
import org.ukwikora.report.Report;

public class Runner {
    private final Project project;
    private final TestFilter filter;
    private final DynamicRuntime runtime;

    Runner(Project project, TestFilter filter){
        this.filter = filter;
        this.project = project;

        this.runtime = new DynamicRuntime(this.project);
    }

    public Report execute(){
        for(TestCase testCase: filter.filter(project.getTestCases())){
            testCase.execute(runtime);
        }

        return runtime.getReport();
    }
}
