package lu.uni.serval.robotframework.libraries.selenium.frame;

import lu.uni.serval.robotframework.model.LibraryKeyword;
import lu.uni.serval.robotframework.runner.Runtime;

public class CurrentFrameShouldContain extends LibraryKeyword {
    public CurrentFrameShouldContain(){
        this.type = Type.Assertion;
    }

    @Override
    public void execute(Runtime runtime) {

    }
}
