package org.ukwikora.libraries.selenium.element;

import org.ukwikora.model.LibraryKeyword;
import org.ukwikora.runner.Runtime;

public class ElementShouldNotBeVisible extends LibraryKeyword {
    public ElementShouldNotBeVisible(){
        this.type = Type.Assertion;
    }

    @Override
    public void run(Runtime runtime) {

    }
}
