package lu.uni.serval.robotframework.parser;

import lu.uni.serval.robotframework.model.Variable;
import lu.uni.serval.robotframework.model.VariableTable;

import java.io.BufferedReader;
import java.io.IOException;

public class VariableTableParser {
    private VariableTableParser() {}

    static public String parse(BufferedReader bufferedReader, VariableTable variableTable) throws IOException {
        String line = bufferedReader.readLine();

        while(line != null){
            if(ParsingUtils.isBlock(line)){
                break;
            }

            if(line.isEmpty()){
                line = bufferedReader.readLine();
                continue;
            }

            String[] tokens = ParsingUtils.tokenizeLine(line);

            Variable variable = new Variable();
            variable.setName(tokens[0]);

            for (int i = 1; i < tokens.length; ++i) {
                variable.addDefinitionElement(tokens[i]);
            }

            variableTable.put(variable.getName(), variable);
            line = bufferedReader.readLine();
        }

        return line;
    }
}
