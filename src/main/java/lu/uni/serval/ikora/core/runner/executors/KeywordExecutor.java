package lu.uni.serval.ikora.core.runner.executors;

import lu.uni.serval.ikora.core.model.*;
import lu.uni.serval.ikora.core.libraries.LibraryKeyword;
import lu.uni.serval.ikora.core.runner.Runtime;
import lu.uni.serval.ikora.core.runner.exception.RunnerException;

public class KeywordExecutor extends NodeExecutor {
    private final Keyword keyword;

    public KeywordExecutor(Runtime runtime, Keyword keyword) {
        super(runtime, keyword);
        this.keyword = keyword;
    }

    @Override
    protected void executeImpl() throws RunnerException {
        if(keyword instanceof UserKeyword){
            executeImpl((UserKeyword) keyword);
        }
        else if(keyword instanceof LibraryKeyword){
            executeImpl((LibraryKeyword)keyword);
        }
    }

    private void executeImpl(UserKeyword userKeyword) throws RunnerException{
        for(Step step: userKeyword.getSteps()){
            new StepExecutor(runtime, step).execute();
        }
    }

    private void executeImpl(LibraryKeyword libraryKeyword) throws RunnerException {
        libraryKeyword.execute(runtime);
    }
}
