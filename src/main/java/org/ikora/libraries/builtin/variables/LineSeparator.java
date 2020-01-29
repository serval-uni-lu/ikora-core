package org.ikora.libraries.builtin.variables;

import org.ikora.model.LibraryVariable;
import org.ikora.model.Token;

public class LineSeparator extends LibraryVariable {
    @Override
    public Token getName() {
        return Token.fromString("${\\n}");
    }
}
