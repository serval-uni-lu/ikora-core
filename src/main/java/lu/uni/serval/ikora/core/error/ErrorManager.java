package lu.uni.serval.ikora.core.error;

/*-
 * #%L
 * Ikora Core
 * %%
 * Copyright (C) 2019 - 2021 University of Luxembourg
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License")
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import lu.uni.serval.ikora.core.model.Range;
import lu.uni.serval.ikora.core.model.Source;

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
        return sourceErrors.isEmpty() && libraryErrors.isEmpty() && inMemory.isEmpty();
    }
}
