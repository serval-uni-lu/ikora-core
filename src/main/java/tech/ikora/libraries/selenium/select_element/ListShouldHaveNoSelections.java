package tech.ikora.libraries.selenium.select_element;

import tech.ikora.model.LibraryKeyword;
import tech.ikora.runner.Runtime;

public class ListShouldHaveNoSelections extends LibraryKeyword {
    public ListShouldHaveNoSelections(){
        this.type = Type.ASSERTION;
    }

    @Override
    public void run(Runtime runtime) {
        throw new UnsupportedOperationException();
    }
}
