package org.ikora.libraries.selenium.browserManagement;

import org.ikora.model.LibraryKeyword;
import org.ikora.runner.Runtime;

public class GetSource extends LibraryKeyword {
    public GetSource(){
        this.type = Type.GET;
    }

    @Override
    public void run(Runtime runtime) {
        throw new UnsupportedOperationException();
    }
}
