package lu.uni.serval.robotframework.compiler;

import lu.uni.serval.robotframework.model.ElementTable;
import lu.uni.serval.robotframework.model.LineRange;
import lu.uni.serval.robotframework.model.Variable;
import lu.uni.serval.robotframework.model.VariableFactory;

import java.io.IOException;

public class VariableTableParser {
    private VariableTableParser() {}

    static public ElementTable<Variable> parse(LineReader reader) throws IOException {
        ElementTable<Variable> variableTable = new ElementTable<>();

        reader.readLine();

        while(reader.getCurrent().isValid()){
            if(Utils.isBlock(reader.getCurrent().getText())){
                break;
            }

            if(reader.getCurrent().ignore()){
                reader.readLine();
                continue;
            }

            String[] tokens = reader.getCurrent().tokenize();

            Variable variable = VariableFactory.create(tokens[0]);
            int startLine = reader.getCurrent().getNumber();

            for (int i = 1; i < tokens.length; ++i) {
                variable.addElement(tokens[i]);
            }

            reader.readLine();

            int endLine = reader.getCurrent().getNumber();
            variable.setLineRange(new LineRange(startLine, endLine));

            variableTable.add(variable);
        }

        return variableTable;
    }
}
