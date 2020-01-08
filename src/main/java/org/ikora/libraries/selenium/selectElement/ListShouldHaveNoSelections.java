package org.ikora.libraries.selenium.selectElement;

import org.ikora.model.LibraryKeyword;
import org.ikora.runner.Runtime;

public class ListShouldHaveNoSelections extends LibraryKeyword {
    public ListShouldHaveNoSelections(){
        this.type = Type.ASSERTION;
    }

    @Override
    public void run(Runtime runtime) {
        throw new UnsupportedOperationException();
    }
}
