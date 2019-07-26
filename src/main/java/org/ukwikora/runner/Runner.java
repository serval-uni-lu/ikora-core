package org.ukwikora.runner;

import org.ukwikora.builder.LibraryLoader;
import org.ukwikora.model.Project;
import org.ukwikora.model.Suite;
import org.ukwikora.model.TestCase;
import org.ukwikora.report.Report;

public class Runner {
    private final Project project;
    private final Runtime runtime;

    Runner(Project project){
        this.project = project;

        this.runtime = new Runtime(this.project, new DynamicScope());
        runtime.setLibraries(LibraryLoader.load());
    }

    public Report execute() throws Exception{
        runtime.reset();

        for(Suite suite: project.getSuites()){
            suite.execute(runtime);
        }

        runtime.finish();

        if(!runtime.getReport().isPresent()){
            throw new Exception("Failed to create report during execution");
        }

        return runtime.getReport().get();
    }
}
