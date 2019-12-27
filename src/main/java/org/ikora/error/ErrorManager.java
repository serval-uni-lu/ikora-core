package org.ikora.error;

import org.ikora.model.Position;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ErrorManager {
    private final Map<File, Errors> fileErrors;
    private final Set<LibraryError> libraryErrors;

    public ErrorManager() {
        this.fileErrors = new HashMap<>();
        this.libraryErrors = new HashSet<>();
    }

    public Errors in(File file){
        return fileErrors.getOrDefault(file, new Errors());
    }

    public void registerSyntaxError(File file, String message, Position position){
        Errors errors = fileErrors.getOrDefault(file, new Errors());
        errors.registerSyntaxError(message, position);
        fileErrors.putIfAbsent(file, errors);
    }

    public void registerSymbolError(File file, String message, Position position){
        Errors errors = fileErrors.getOrDefault(file, new Errors());
        errors.registerSymbolError(message, position);
        fileErrors.putIfAbsent(file, errors);
    }

    public void registerIOError(File file, String message){
        Errors errors = fileErrors.getOrDefault(file, new Errors());
        errors.registerIOError(message, file);
        fileErrors.putIfAbsent(file, errors);
    }

    public void registerInternalError(File file, String message, Position position){
        Errors errors = fileErrors.getOrDefault(file, new Errors());
        errors.registerInternalError(message, position);
        fileErrors.putIfAbsent(file, errors);
    }

    public void registerUnhandledError(File file, String message, Exception exception){
        Errors errors = fileErrors.getOrDefault(file, new Errors());
        errors.registerUnhandledError(message, exception);
        fileErrors.putIfAbsent(file, errors);
    }

    public void registerLibraryError(String library, String message){
        libraryErrors.add(new LibraryError(message, library));
    }

    public boolean isEmpty() {
        return fileErrors.isEmpty() && libraryErrors.isEmpty();
    }
}
