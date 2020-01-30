package org.ikora.libraries.builtin.variables;

import org.ikora.model.LibraryVariable;
import org.ikora.model.Token;

import java.util.regex.Pattern;

public class Space extends LibraryVariable {
    private Pattern match;

    public Space(){
        match = Pattern.compile("^\\$\\{SPACE(\\s*\\*\\s*\\d*)?}$", Pattern.CASE_INSENSITIVE);
    }

    @Override
    public boolean matches(Token name) {
        return match.matcher(name.getText()).matches();
    }
}
