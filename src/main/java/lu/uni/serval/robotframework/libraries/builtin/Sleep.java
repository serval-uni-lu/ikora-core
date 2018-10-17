package lu.uni.serval.robotframework.libraries.builtin;

import lu.uni.serval.robotframework.model.LibraryKeyword;
import lu.uni.serval.robotframework.runner.Runtime;

public class Sleep extends LibraryKeyword {
    public Sleep(){
        this.type = Type.Synchronisation;
    }

    @Override
    public void execute(Runtime runtime) {

    }
}
