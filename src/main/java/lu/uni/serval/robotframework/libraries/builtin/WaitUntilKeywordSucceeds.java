package lu.uni.serval.robotframework.libraries.builtin;

import lu.uni.serval.robotframework.model.LibraryKeyword;
import lu.uni.serval.robotframework.runner.Runtime;

public class WaitUntilKeywordSucceeds extends LibraryKeyword {
    public WaitUntilKeywordSucceeds(){
        this.type = Type.Synchronisation;
    }

    @Override
    public void execute(Runtime runtime) {

    }
}
