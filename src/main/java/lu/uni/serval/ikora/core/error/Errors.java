/*
 *
 *     Copyright © 2019 - 2022 University of Luxembourg
 *
 *     Licensed under the Apache License, Version 2.0 (the "License")
 *     you may not use this file except in compliance with the License.
 *     You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *     Unless required by applicable law or agreed to in writing, software
 *     distributed under the License is distributed on an "AS IS" BASIS,
 *     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *     See the License for the specific language governing permissions and
 *     limitations under the License.
 */
package lu.uni.serval.ikora.core.error;

import lu.uni.serval.ikora.core.model.Range;
import lu.uni.serval.ikora.core.model.Source;

import java.util.HashSet;
import java.util.Set;

public class Errors {
    private final Set<SyntaxError> syntaxErrors;
    private final Set<SymbolError> symbolErrors;
    private final Set<InternalError> internalErrors;
    private final Set<IOError> ioErrors;
    private final Set<UnhandledError> unhandledErrors;

    Errors(){
        syntaxErrors = new HashSet<>();
        symbolErrors = new HashSet<>();
        ioErrors = new HashSet<>();
        internalErrors = new HashSet<>();
        unhandledErrors = new HashSet<>();
    }

    public void registerSyntaxError(String message, Range range){
        SyntaxError error = new SyntaxError(message, range);
        syntaxErrors.add(error);
    }

    public void registerSymbolError(String message, Range range){
        SymbolError error = new SymbolError(message, range);
        symbolErrors.add(error);
    }

    public void registerIOError(String message, Source source){
        IOError error = new IOError(message, source);
        ioErrors.add(error);
    }

    public void registerInternalError(String message, Range range){
        InternalError error = new InternalError(message, range);
        internalErrors.add(error);
    }

    public void  registerUnhandledError(String message, Exception exception){
        UnhandledError error = new UnhandledError(message, exception);
        unhandledErrors.add(error);
    }

    public int getSize(){
        return syntaxErrors.size()
                + symbolErrors.size()
                + ioErrors.size()
                + internalErrors.size()
                + unhandledErrors.size();
    }

    public Set<SyntaxError> getSyntaxErrors() {
        return syntaxErrors;
    }

    public Set<SymbolError> getSymbolErrors() {
        return symbolErrors;
    }

    public Set<InternalError> getInternalErrors() {
        return internalErrors;
    }

    public Set<IOError> getIoErrors() {
        return ioErrors;
    }

    public Set<UnhandledError> getUnhandledErrors() {
        return unhandledErrors;
    }

    public Set<Error> getAll(){
        Set<Error> all = new HashSet<>(getSize());

        all.addAll(syntaxErrors);
        all.addAll(symbolErrors);
        all.addAll(ioErrors);
        all.addAll(internalErrors);
        all.addAll(unhandledErrors);

        return all;
    }

    public boolean isEmpty() {
        return syntaxErrors.isEmpty()
                && symbolErrors.isEmpty()
                && ioErrors.isEmpty()
                && internalErrors.isEmpty()
                && unhandledErrors.isEmpty();
    }
}
