package tech.ikora.libraries.builtin;

import tech.ikora.model.LibraryKeyword;
import tech.ikora.runner.Runtime;

public class SetTestMessage extends LibraryKeyword {
    public SetTestMessage(){
        this.type = Type.SET;
    }

    @Override
    public void run(Runtime runtime) {
        throw new UnsupportedOperationException();
    }
}