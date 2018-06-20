package lu.uni.serval.robotframework.compiler;

import lu.uni.serval.robotframework.model.Variable;
import lu.uni.serval.robotframework.model.VariableTable;

import java.io.LineNumberReader;
import java.io.IOException;

public class VariableTableParser {
    private VariableTableParser() {}

    static public Line parse(LineNumberReader reader, VariableTable variableTable) throws IOException {
        Line line = Line.getNextLine(reader);

        while(line.isValid()){
            if(Utils.isBlock(line.getText())){
                break;
            }

            if(line.isEmpty()){
                line = Line.getNextLine(reader);
                continue;
            }

            String[] tokens = line.tokenize();

            Variable variable = new Variable();
            variable.setName(tokens[0]);

            for (int i = 1; i < tokens.length; ++i) {
                variable.addValueElement(tokens[i]);
            }

            variableTable.put(variable.getName(), variable);
            line = Line.getNextLine(reader);
        }

        return line;
    }
}
