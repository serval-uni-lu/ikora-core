package lu.uni.serval.ikora.core.exception;

import lu.uni.serval.ikora.core.error.ErrorManager;

public class ExecutionException extends Exception{
    private final ErrorManager errors;

    public ExecutionException(ErrorManager errors){
        this.errors = errors;
    }

    public ErrorManager getErrors() {
        return errors;
    }
}
