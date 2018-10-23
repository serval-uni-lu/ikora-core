package org.ukwikora.libraries.builtin;

import org.ukwikora.model.LibraryKeyword;
import org.ukwikora.runner.Runtime;

public class KeywordShouldExist extends LibraryKeyword {
    public KeywordShouldExist(){
        this.type = Type.Assertion;
    }

    @Override
    public void execute(Runtime runtime) {

    }
}
