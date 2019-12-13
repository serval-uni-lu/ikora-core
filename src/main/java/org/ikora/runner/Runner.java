package org.ikora.runner;

import org.ikora.builder.LibraryLoader;
import org.ikora.model.Project;
import org.ikora.model.Suite;
import org.ikora.report.Report;

import java.util.Optional;

public class Runner {
    private final Project project;
    private final Runtime runtime;

    Runner(Project project){
        this.project = project;

        this.runtime = new Runtime(this.project, new DynamicScope());
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
            throw new Exception("Failed to create report during execution");
        }

        return report.get();
    }
}
