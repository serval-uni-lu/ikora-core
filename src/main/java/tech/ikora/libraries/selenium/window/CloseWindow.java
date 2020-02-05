package tech.ikora.libraries.selenium.window;

import tech.ikora.model.LibraryKeyword;
import tech.ikora.runner.Runtime;

public class CloseWindow extends LibraryKeyword {
    public CloseWindow(){
        this.type = Type.ACTION;
    }

    @Override
    public void run(Runtime runtime) {
        throw new UnsupportedOperationException();
    }
}
