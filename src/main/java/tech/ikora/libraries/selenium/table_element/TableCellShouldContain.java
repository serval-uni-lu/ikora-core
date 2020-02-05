package tech.ikora.libraries.selenium.table_element;

import tech.ikora.model.LibraryKeyword;
import tech.ikora.runner.Runtime;

public class TableCellShouldContain extends LibraryKeyword {
    public TableCellShouldContain(){
        this.type = Type.ASSERTION;
    }

    @Override
    public void run(Runtime runtime) {
        throw new UnsupportedOperationException();
    }
}
