package org.ukwikora.libraries.builtin.variables;

import org.ukwikora.model.LibraryVariable;

import java.util.regex.Pattern;

public class Space extends LibraryVariable {
    private Pattern match;

    public Space(){
        match = Pattern.compile("^\\$\\{SPACE(\\s*\\*\\s*\\d*)?\\}$", Pattern.CASE_INSENSITIVE);
    }

    @Override
    public boolean matches(String name) {
        return match.matcher(name).matches();
    }
}
