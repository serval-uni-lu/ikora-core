package org.ikora.libraries.builtin;

import org.ikora.model.Argument;
import org.ikora.model.Value;
import org.ikora.model.LibraryKeyword;
import org.ikora.runner.Runtime;

public class ShouldBeEqual extends LibraryKeyword {
    public ShouldBeEqual(){
        this.type = Type.ASSERTION;
    }

    @Override
    public void run(Runtime runtime) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Argument.Type[] getArgumentTypes() {
        return new Argument.Type[]{
                Argument.Type.STRING,
                Argument.Type.STRING
        };
    }
}
