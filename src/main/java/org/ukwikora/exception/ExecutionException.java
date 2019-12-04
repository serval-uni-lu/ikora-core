package org.ukwikora.exception;

import org.ukwikora.error.ErrorManager;

public class ExecutionException extends Exception{
    private final ErrorManager errors;

    public ExecutionException(ErrorManager errors){
        this.errors = errors;
    }

    public ErrorManager getErrors() {
        return errors;
    }
}
