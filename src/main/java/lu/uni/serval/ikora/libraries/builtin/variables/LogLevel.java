package lu.uni.serval.ikora.libraries.builtin.variables;

import lu.uni.serval.ikora.model.LibraryVariable;
import lu.uni.serval.ikora.types.LogLevelType;

public class LogLevel extends LibraryVariable {
    public LogLevel(){
        super(new LogLevelType("LOG LEVEL"), Format.SCALAR);
    }
}
