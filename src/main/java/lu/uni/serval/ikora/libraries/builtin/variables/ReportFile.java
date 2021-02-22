package lu.uni.serval.ikora.libraries.builtin.variables;

import lu.uni.serval.ikora.model.LibraryVariable;
import lu.uni.serval.ikora.types.StringType;

public class ReportFile extends LibraryVariable {
    public ReportFile(){
        super(new StringType("REPORT FILE"), Format.SCALAR);
    }
}
