package org.ikora.error;

import org.ikora.model.Position;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ErrorManager {
    private final Map<File, ErrorFile> errors;
    private final Set<LibraryError> libraryErrors;

    public ErrorManager() {
        this.errors = new HashMap<>();
        this.libraryErrors = new HashSet<>();
    }

    public void registerSyntaxError(File file, String message, Position position){
        ErrorFile errorFile = errors.getOrDefault(file, new ErrorFile());
        errorFile.registerSyntaxError(message, position);
        errors.putIfAbsent(file, errorFile);
    }

    public void registerSymbolError(File file, String message, Position position){
        ErrorFile errorFile = errors.getOrDefault(file, new ErrorFile());
        SymbolError error = new SymbolError(message, position);
        errors.putIfAbsent(file, errorFile);
    }

    public void registerIOError(File file, String message){
        ErrorFile errorFile = errors.getOrDefault(file, new ErrorFile());
        errorFile.registerIOError(message, file);
        errors.putIfAbsent(file, errorFile);
    }

    public void registerInternalError(File file, String message, Position position){
        ErrorFile errorFile = errors.getOrDefault(file, new ErrorFile());
        errorFile.registerInternalError(message, position);
        errors.putIfAbsent(file, errorFile);
    }

    public void registerUnhandledError(File file, String message, Exception exception){
        ErrorFile errorFile = errors.getOrDefault(file, new ErrorFile());
        errorFile.registerUnhandledError(message, exception);
        errors.putIfAbsent(file, errorFile);
    }

    public void registerLibraryError(String library, String message){
        libraryErrors.add(new LibraryError(message, library));
    }

    public boolean isEmpty() {
        return errors.isEmpty() && libraryErrors.isEmpty();
    }
}
