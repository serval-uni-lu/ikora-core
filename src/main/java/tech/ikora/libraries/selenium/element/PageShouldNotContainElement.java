package tech.ikora.libraries.selenium.element;

import tech.ikora.model.LibraryKeyword;
import tech.ikora.runner.Runtime;

public class PageShouldNotContainElement extends LibraryKeyword {
    public PageShouldNotContainElement(){
        this.type = Type.ASSERTION;
    }

    @Override
    public void run(Runtime runtime) {
        throw new UnsupportedOperationException();
    }
}
