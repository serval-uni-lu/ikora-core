package org.ikora.libraries.selenium.frame;

import org.ikora.model.LibraryKeyword;
import org.ikora.runner.Runtime;

public class CurrentFrameShouldContain extends LibraryKeyword {
    public CurrentFrameShouldContain(){
        this.type = Type.Assertion;
    }

    @Override
    public void run(Runtime runtime) {

    }
}
