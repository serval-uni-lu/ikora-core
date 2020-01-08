package org.ikora.libraries.selenium.tableElement;

import org.ikora.model.LibraryKeyword;
import org.ikora.runner.Runtime;

public class TableShouldContain extends LibraryKeyword {
    public TableShouldContain(){
        this.type = Type.ASSERTION;
    }

    @Override
    public void run(Runtime runtime) {
        throw new UnsupportedOperationException();
    }
}
