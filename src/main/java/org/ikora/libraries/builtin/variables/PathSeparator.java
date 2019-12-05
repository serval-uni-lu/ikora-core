package org.ikora.libraries.builtin.variables;

import org.ikora.model.LibraryVariable;

public class PathSeparator extends LibraryVariable {
    @Override
    public String getName() {
        return "${/}";
    }
}
