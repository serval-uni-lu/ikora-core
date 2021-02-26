package lu.uni.serval.ikora.core.libraries.builtin.variables;

import lu.uni.serval.ikora.core.model.LibraryVariable;
import lu.uni.serval.ikora.core.types.LogLevelType;

public class LogLevel extends LibraryVariable {
    public LogLevel(){
        super(new LogLevelType("LOG LEVEL"), Format.SCALAR);
    }
}
