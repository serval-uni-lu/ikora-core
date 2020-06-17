package tech.ikora.libraries.builtin.variables;

import tech.ikora.model.LibraryVariable;
import tech.ikora.types.StringType;

public class ReportFile extends LibraryVariable {
    public ReportFile(){
        super(new StringType("REPORT FILE"), Format.SCALAR);
    }
}
