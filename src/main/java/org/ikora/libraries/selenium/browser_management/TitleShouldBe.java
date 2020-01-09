package org.ikora.libraries.selenium.browser_management;

import org.ikora.model.LibraryKeyword;
import org.ikora.runner.Runtime;

public class TitleShouldBe extends LibraryKeyword {
    public TitleShouldBe(){
        this.type = Type.ASSERTION;
    }

    @Override
    public void run(Runtime runtime) {
        throw new UnsupportedOperationException();
    }
}
