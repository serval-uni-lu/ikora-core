package lu.uni.serval.ikora.runner;

import lu.uni.serval.ikora.builder.LibraryLoader;
import lu.uni.serval.ikora.error.ErrorManager;
import lu.uni.serval.ikora.model.Project;
import lu.uni.serval.ikora.model.Suite;
import lu.uni.serval.ikora.report.Report;

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
