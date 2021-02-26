package lu.uni.serval.ikora.core.runner;

import lu.uni.serval.ikora.core.builder.LibraryLoader;
import lu.uni.serval.ikora.core.error.ErrorManager;
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
            throw new Exception("Failed to create report during execution");
        }

        return report.get();
    }
}
