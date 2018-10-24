package org.ukwikora.libraries.selenium.element;

import org.ukwikora.model.LibraryKeyword;
import org.ukwikora.model.Runtime;

public class PageShouldNotContainElement extends LibraryKeyword {
    public PageShouldNotContainElement(){
        this.type = Type.Assertion;
    }

    @Override
    public void execute(Runtime runtime) {

    }
}
