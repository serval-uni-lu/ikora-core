package org.ikora.libraries.selenium.element;

import org.ikora.model.Argument;
import org.ikora.model.LibraryKeyword;
import org.ikora.runner.Runtime;

public class PageShouldContainElement extends LibraryKeyword {
    public PageShouldContainElement(){
        this.type = Type.ASSERTION;
    }

    @Override
    public void run(Runtime runtime) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Argument.Type[] getArgumentTypes() {
        return new Argument.Type[]{
                Argument.Type.LOCATOR
        };
    }
}
