package tech.ikora.libraries.builtin.variables;

import tech.ikora.model.LibraryVariable;
import tech.ikora.types.LogLevelType;

public class LogLevel extends LibraryVariable {
    public LogLevel(){
        super(new LogLevelType("LOG LEVEL"), Format.SCALAR);
    }
}
