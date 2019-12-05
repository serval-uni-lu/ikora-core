package org.ikora.libraries.selenium.element;

import org.ikora.model.LibraryKeyword;
import org.ikora.runner.Runtime;

public class PageShouldNotContainElement extends LibraryKeyword {
    public PageShouldNotContainElement(){
        this.type = Type.Assertion;
    }

    @Override
    public void run(Runtime runtime) {

    }
}
