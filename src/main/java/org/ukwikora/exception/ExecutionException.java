package org.ukwikora.exception;

import org.ukwikora.error.Error;

import java.util.List;

public class ExecutionException extends Exception{
    private final List<Error> errors;

    public ExecutionException(List<Error> errors){
        this.errors = errors;
    }
}
