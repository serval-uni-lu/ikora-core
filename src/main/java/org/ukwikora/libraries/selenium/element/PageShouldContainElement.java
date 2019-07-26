package org.ukwikora.libraries.selenium.element;

import org.ukwikora.model.Value;
import org.ukwikora.model.LibraryKeyword;
import org.ukwikora.runner.Runtime;

public class PageShouldContainElement extends LibraryKeyword {
    public PageShouldContainElement(){
        this.type = Type.Assertion;
    }

    @Override
    public void run(Runtime runtime) {

    }

    @Override
    public Value.Type[] getArgumentTypes() {
        return new Value.Type[]{
                Value.Type.Locator
        };
    }
}
