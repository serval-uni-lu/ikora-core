package org.ikora.libraries.selenium.element;

import org.ikora.model.Value;
import org.ikora.model.LibraryKeyword;
import org.ikora.runner.Runtime;

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
