package lu.uni.serval.robotframework.libraries.builtin;

import lu.uni.serval.robotframework.model.LibraryKeyword;
import lu.uni.serval.robotframework.runner.Runtime;

public class ShouldBeTrue extends LibraryKeyword {
    public ShouldBeTrue(){
        this.type = Type.Assertion;
    }

    @Override
    public void execute(Runtime runtime) {

    }
}
