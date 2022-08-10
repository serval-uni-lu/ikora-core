package lu.uni.serval.ikora.core.runner.executors;

import lu.uni.serval.ikora.core.libraries.builtin.keywords.Log;
import lu.uni.serval.ikora.core.model.Keyword;
import lu.uni.serval.ikora.core.model.LibraryKeyword;
import lu.uni.serval.ikora.core.model.Step;
import lu.uni.serval.ikora.core.model.UserKeyword;
import lu.uni.serval.ikora.core.runner.Runtime;
import lu.uni.serval.ikora.core.runner.exception.RunnerException;
import lu.uni.serval.ikora.core.runner.executors.libary.LogExecutor;

public class KeywordExecutor extends BaseExecutor {
    public KeywordExecutor(Runtime runtime) {
        super(runtime);
    }

    public void execute(Keyword keyword) throws RunnerException {
        if(keyword instanceof UserKeyword){
            execute((UserKeyword) keyword);
        }
        else if(keyword instanceof LibraryKeyword){
            execute((LibraryKeyword)keyword);
        }
    }

    private void execute(UserKeyword userKeyword) throws RunnerException{
        try{
            runtime.enterNode(userKeyword);

            final StepExecutor executor = new StepExecutor(runtime);

            for(Step step: userKeyword.getSteps()){
                executor.execute(step);
            }
        }
        finally {
            runtime.exitNode(userKeyword);
        }
    }

    private void execute(LibraryKeyword libraryKeyword) throws RunnerException {
        runtime.enterNode(libraryKeyword);

        if(libraryKeyword instanceof Log){
            new LogExecutor(runtime).execute();
        }

        runtime.exitNode(libraryKeyword);
    }
}
