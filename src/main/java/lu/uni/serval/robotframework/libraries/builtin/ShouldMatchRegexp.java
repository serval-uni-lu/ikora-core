package lu.uni.serval.robotframework.libraries.builtin;

import lu.uni.serval.robotframework.model.LibraryKeyword;
import lu.uni.serval.robotframework.runner.Runtime;

public class ShouldMatchRegexp extends LibraryKeyword {
    public ShouldMatchRegexp(){
        this.type = Type.Assertion;
    }

    @Override
    public void execute(Runtime runtime) {

    }
}
