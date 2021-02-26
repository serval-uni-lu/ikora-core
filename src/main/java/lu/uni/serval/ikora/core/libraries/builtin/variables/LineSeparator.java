package lu.uni.serval.ikora.core.libraries.builtin.variables;

import lu.uni.serval.ikora.core.model.LibraryVariable;
import lu.uni.serval.ikora.core.types.StringType;

public class LineSeparator extends LibraryVariable {
    public LineSeparator(){
        super(new StringType("\\n"), Format.SCALAR);
    }
}
