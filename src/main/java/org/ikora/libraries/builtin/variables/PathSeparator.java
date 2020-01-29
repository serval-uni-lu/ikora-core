package org.ikora.libraries.builtin.variables;

import org.ikora.model.LibraryVariable;
import org.ikora.model.Token;

public class PathSeparator extends LibraryVariable {
    @Override
    public Token getName() {
        return Token.fromString("${/}");
    }
}
