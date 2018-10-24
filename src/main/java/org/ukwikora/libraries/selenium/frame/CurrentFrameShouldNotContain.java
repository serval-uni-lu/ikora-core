package org.ukwikora.libraries.selenium.frame;

import org.ukwikora.model.LibraryKeyword;
import org.ukwikora.model.Runtime;

public class CurrentFrameShouldNotContain extends LibraryKeyword {
    public CurrentFrameShouldNotContain(){
        this.type = Type.Assertion;
    }

    @Override
    public void execute(Runtime runtime) {

    }
}
