package org.ukwikora.libraries.selenium.frame;

import org.ukwikora.model.LibraryKeyword;
import org.ukwikora.model.Runtime;

public class CurrentFrameContains extends LibraryKeyword {
    public CurrentFrameContains(){
        this.type = Type.Assertion;
    }

    @Override
    public void execute(Runtime runtime) {

    }
}
