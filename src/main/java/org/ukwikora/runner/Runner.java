package org.ukwikora.runner;

import org.ukwikora.model.Project;
import org.ukwikora.model.TestCase;
import org.ukwikora.report.Report;

public class Runner {
    private final Project project;
    private final TestFilter filter;
    private final Runtime runtime;

    Runner(Project project, TestFilter filter){
        this.filter = filter;
        this.project = project;

        this.runtime = new Runtime(this.project, new DynamicScope());
    }

    public Report execute() throws Exception{
        runtime.reset();

        for(TestCase testCase: filter.filter(project.getTestCases())){
            testCase.execute(runtime);
        }

        if(!runtime.getReport().isPresent()){
            throw new Exception("Failed to create report during execution");
        }

        runtime.finish();

        return runtime.getReport().get();
    }
}
