package lu.uni.serval.robotframework.compiler;

import lu.uni.serval.robotframework.model.Variable;
import lu.uni.serval.robotframework.model.VariableTable;

import java.io.IOException;

public class VariableTableParser {
    private VariableTableParser() {}

    static public VariableTable parse(LineReader reader) throws IOException {
        VariableTable variableTable = new VariableTable();

        reader.readLine();

        while(reader.getCurrent().isValid()){
            if(Utils.isBlock(reader.getCurrent().getText())){
                break;
            }

            if(reader.getCurrent().isEmpty()){
                reader.readLine();
                continue;
            }

            String[] tokens = reader.getCurrent().tokenize();

            Variable variable = new Variable();
            variable.setName(tokens[0]);

            for (int i = 1; i < tokens.length; ++i) {
                variable.addValueElement(tokens[i]);
            }

            variableTable.put(variable.getName(), variable);
            reader.readLine();
        }

        return variableTable;
    }
}
