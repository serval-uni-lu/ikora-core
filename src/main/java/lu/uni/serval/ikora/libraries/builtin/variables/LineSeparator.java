package lu.uni.serval.ikora.libraries.builtin.variables;

import lu.uni.serval.ikora.model.LibraryVariable;
import lu.uni.serval.ikora.types.StringType;

public class LineSeparator extends LibraryVariable {
    public LineSeparator(){
        super(new StringType("\\n"), Format.SCALAR);
    }
}
