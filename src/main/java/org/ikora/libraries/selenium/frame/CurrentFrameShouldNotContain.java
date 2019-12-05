package org.ikora.libraries.selenium.frame;

import org.ikora.model.LibraryKeyword;
import org.ikora.runner.Runtime;

public class CurrentFrameShouldNotContain extends LibraryKeyword {
    public CurrentFrameShouldNotContain(){
        this.type = Type.Assertion;
    }

    @Override
    public void run(Runtime runtime) {

    }
}
