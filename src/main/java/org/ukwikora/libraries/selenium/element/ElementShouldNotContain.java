package org.ukwikora.libraries.selenium.element;

import org.ukwikora.model.LibraryKeyword;
import org.ukwikora.model.Runtime;

public class ElementShouldNotContain extends LibraryKeyword {
    public ElementShouldNotContain(){
        this.type = Type.Assertion;
    }

    @Override
    public void execute(Runtime runtime) {

    }
}
