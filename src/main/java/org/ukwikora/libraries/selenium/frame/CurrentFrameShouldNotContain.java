package org.ukwikora.libraries.selenium.frame;

import org.ukwikora.model.LibraryKeyword;
import org.ukwikora.runner.Runtime;

public class CurrentFrameShouldNotContain extends LibraryKeyword {
    public CurrentFrameShouldNotContain(){
        this.type = Type.Assertion;
    }

    @Override
    public void run(Runtime runtime) {

    }
}
