package lu.uni.serval.robotframework.libraries.builtin;

import lu.uni.serval.robotframework.model.LibraryKeyword;
import lu.uni.serval.robotframework.runner.Runtime;

public class ShouldNotMatchRegexp extends LibraryKeyword {
    public ShouldNotMatchRegexp(){
        this.type = Type.Assertion;
    }

    @Override
    public void execute(Runtime runtime) {

    }
}
