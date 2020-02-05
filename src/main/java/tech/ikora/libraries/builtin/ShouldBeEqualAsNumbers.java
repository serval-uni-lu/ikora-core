package tech.ikora.libraries.builtin;

import tech.ikora.model.LibraryKeyword;
import tech.ikora.runner.Runtime;

public class ShouldBeEqualAsNumbers extends LibraryKeyword {
    public ShouldBeEqualAsNumbers(){
        this.type = Type.ASSERTION;
    }

    @Override
    public void run(Runtime runtime) {
        throw new UnsupportedOperationException();
    }
}
