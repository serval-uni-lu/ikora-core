package org.ikora.libraries.builtin.variables;

import org.ikora.model.LibraryVariable;

public class LineSeparator extends LibraryVariable {
    @Override
    public String getName() {
        return "${\\n}";
    }
}
