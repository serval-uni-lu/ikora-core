package lu.uni.serval.robotframework.libraries.selenium.element;

import lu.uni.serval.robotframework.model.LibraryKeyword;
import lu.uni.serval.robotframework.runner.Runtime;

public class PageShouldNotContainElement extends LibraryKeyword {
    public PageShouldNotContainElement(){
        this.type = Type.Assertion;
    }

    @Override
    public void execute(Runtime runtime) {

    }
}
