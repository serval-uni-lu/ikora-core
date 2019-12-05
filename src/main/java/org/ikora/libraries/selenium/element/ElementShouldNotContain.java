package org.ikora.libraries.selenium.element;

import org.ikora.model.LibraryKeyword;
import org.ikora.runner.Runtime;

public class ElementShouldNotContain extends LibraryKeyword {
    public ElementShouldNotContain(){
        this.type = Type.Assertion;
    }

    @Override
    public void run(Runtime runtime) {

    }
}
