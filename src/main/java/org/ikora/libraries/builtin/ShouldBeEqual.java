package org.ikora.libraries.builtin;

import org.ikora.model.Value;
import org.ikora.model.LibraryKeyword;
import org.ikora.runner.Runtime;

public class ShouldBeEqual extends LibraryKeyword {
    public ShouldBeEqual(){
        this.type = Type.Assertion;
    }

    @Override
    public void run(Runtime runtime) {

    }

    @Override
    public Value.Type[] getArgumentTypes() {
        return new Value.Type[]{
                Value.Type.String,
                Value.Type.String
        };
    }
}
