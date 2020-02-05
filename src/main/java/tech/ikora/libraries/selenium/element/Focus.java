package tech.ikora.libraries.selenium.element;

import tech.ikora.model.LibraryKeyword;
import tech.ikora.runner.Runtime;

public class Focus extends LibraryKeyword {
    public Focus(){
        this.type = Type.ACTION;
    }

    @Override
    public void run(Runtime runtime) {
        throw new UnsupportedOperationException();
    }
}
