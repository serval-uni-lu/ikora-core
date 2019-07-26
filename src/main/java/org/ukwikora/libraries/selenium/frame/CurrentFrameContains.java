package org.ukwikora.libraries.selenium.frame;

import org.ukwikora.model.LibraryKeyword;
import org.ukwikora.runner.Runtime;

public class CurrentFrameContains extends LibraryKeyword {
    public CurrentFrameContains(){
        this.type = Type.Assertion;
    }

    @Override
    public void run(Runtime runtime) {

    }
}
