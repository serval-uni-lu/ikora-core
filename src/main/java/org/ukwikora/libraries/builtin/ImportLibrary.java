package org.ukwikora.libraries.builtin;

import org.ukwikora.model.*;
import org.ukwikora.runner.Runtime;

public class ImportLibrary extends LibraryKeyword implements ScopeModifier {
    @Override
    public void run(Runtime runtime) {

    }

    @Override
    public void addToScope(Runtime runtime, KeywordCall call) throws Exception {
        KeywordDefinition parent = null;
        Keyword current = call.getParent();

        while(current != null){
            if(current.getClass() == Step.class) {
                current = ((Step) current).getParent();
            }
            else if(current.getClass() == KeywordDefinition.class){
                parent = (KeywordDefinition) current;
                break;
            }
            else{
                throw new Exception(String.format("Failed to resolve dynamic import in file %s, lines %s",
                        call.getFileName(),
                        call.getLineRange().toString()));
            }
        }

        if(parent != null){
            runtime.addDynamicLibrary(parent, call.getParameters());
        }
    }
}
