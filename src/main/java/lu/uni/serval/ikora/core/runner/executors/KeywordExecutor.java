package lu.uni.serval.ikora.core.runner.executors;

import lu.uni.serval.ikora.core.model.*;
import lu.uni.serval.ikora.core.libraries.LibraryKeyword;
import lu.uni.serval.ikora.core.parser.ValueParser;
import lu.uni.serval.ikora.core.runner.ArgumentFetcher;
import lu.uni.serval.ikora.core.runner.Runtime;
import lu.uni.serval.ikora.core.runner.exception.RunnerException;

import java.util.Collections;

public class KeywordExecutor extends NodeExecutor {
    private final Keyword keyword;

    public KeywordExecutor(Runtime runtime, Keyword keyword) {
        super(runtime, keyword);
        this.keyword = keyword;
    }

    @Override
    protected void executeImpl() throws RunnerException {
        if(keyword instanceof UserKeyword user){
            executeImpl(user);
        }
        else if(keyword instanceof LibraryKeyword library){
            executeImpl(library);
        }
    }

    private void executeImpl(UserKeyword userKeyword) throws RunnerException{
        for(Argument argument: userKeyword.getArguments()){
            if(!(Variable.class.isAssignableFrom(argument.getDefinition().getClass()))){
                throw new RunnerException("Expected an variable got " + argument.getName() + " instead!");
            }

            final Variable variable = (Variable) argument.getDefinition();
            final String valueString = ArgumentFetcher.fetch(runtime.getArguments(), variable.getName(), userKeyword.getArgumentTypes(), String.class);
            final Value value = ValueParser.parseValue(Token.fromString(valueString));

            runtime.addToKeywordScope(new VariableAssignment(variable, Collections.singletonList(value)));
        }

        for(Step step: userKeyword.getSteps()){
            new StepExecutor(runtime, step).execute();
        }
    }

    private void executeImpl(LibraryKeyword libraryKeyword) throws RunnerException {
        libraryKeyword.execute(runtime);
    }
}
