package tech.ikora.error;

import tech.ikora.model.Range;
import tech.ikora.model.Source;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ErrorManager {
    private final Errors inMemory;
    private final Map<Source, Errors> sourceErrors;
    private final Set<LibraryError> libraryErrors;

    public ErrorManager() {
        inMemory = new Errors();
        this.sourceErrors = new HashMap<>();
        this.libraryErrors = new HashSet<>();
    }

    public Errors in(Source source){
        return sourceErrors.getOrDefault(source, new Errors());
    }

    public Errors inMemory(){
        return inMemory;
    }

    public void registerSyntaxError(Source source, String message, Range range){
        if(source == null || source.isInMemory()){
            inMemory.registerSyntaxError(message, range);
        }
        else{
            Errors errors = sourceErrors.getOrDefault(source, new Errors());
            errors.registerSyntaxError(message, range);
            sourceErrors.putIfAbsent(source, errors);
        }

    }

    public void registerSymbolError(Source source, String message, Range range){
        if(source == null || source.isInMemory()){
            inMemory.registerSymbolError(message, range);
        }
        else {
            Errors errors = sourceErrors.getOrDefault(source, new Errors());
            errors.registerSymbolError(message, range);
            sourceErrors.putIfAbsent(source, errors);
        }
    }

    public void registerIOError(Source source, String message){
        if(source == null || source.isInMemory()){
            inMemory.registerIOError(message, source);
        }
        else {
            Errors errors = sourceErrors.getOrDefault(source, new Errors());
            errors.registerIOError(message, source);
            sourceErrors.putIfAbsent(source, errors);
        }
    }

    public void registerInternalError(Source source, String message, Range range){
        if(source == null || source.isInMemory()){
            inMemory.registerInternalError(message, range);
        }
        else {
            Errors errors = sourceErrors.getOrDefault(source, new Errors());
            errors.registerInternalError(message, range);
            sourceErrors.putIfAbsent(source, errors);
        }
    }

    public void registerUnhandledError(Source source, String message, Exception exception){
        if(source == null || source.isInMemory()){
            inMemory.registerUnhandledError(message, exception);
        }
        else {
            Errors errors = sourceErrors.getOrDefault(source, new Errors());
            errors.registerUnhandledError(message, exception);
            sourceErrors.putIfAbsent(source, errors);
        }
    }

    public void registerLibraryError(String library, String message){
        libraryErrors.add(new LibraryError(message, library));
    }

    public boolean isEmpty() {
        return sourceErrors.isEmpty() && libraryErrors.isEmpty();
    }
}
