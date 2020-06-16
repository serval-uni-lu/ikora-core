package tech.ikora.libraries.builtin.keywords;

import tech.ikora.model.LibraryKeyword;
import tech.ikora.runner.Runtime;

import java.util.Collections;

public class ExitForLoop extends LibraryKeyword {
    public ExitForLoop(){
        super(Type.CONTROL_FLOW, Collections.emptyList());
    }

    @Override
    public void run(Runtime runtime) {
        throw new UnsupportedOperationException();
    }
}
