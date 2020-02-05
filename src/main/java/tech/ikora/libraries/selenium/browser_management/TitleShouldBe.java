package tech.ikora.libraries.selenium.browser_management;

import tech.ikora.model.LibraryKeyword;
import tech.ikora.runner.Runtime;

public class TitleShouldBe extends LibraryKeyword {
    public TitleShouldBe(){
        this.type = Type.ASSERTION;
    }

    @Override
    public void run(Runtime runtime) {
        throw new UnsupportedOperationException();
    }
}
