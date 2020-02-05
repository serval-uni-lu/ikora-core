package tech.ikora.libraries.selenium.alert;

import tech.ikora.model.LibraryKeyword;
import tech.ikora.runner.Runtime;

public class HandleAlert extends LibraryKeyword {
    public HandleAlert(){
        this.type = Type.ACTION;
    }

    @Override
    public void run(Runtime runtime) {
        throw new UnsupportedOperationException();
    }
}
