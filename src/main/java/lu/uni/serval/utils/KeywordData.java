package lu.uni.serval.utils;

import lu.uni.serval.robotframework.model.Argument;
import lu.uni.serval.utils.tree.TreeNodeData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KeywordData implements TreeNodeData {
    public String name;
    public List<String> arguments;
    public Map<String, List<String>> variables;
    public String file;
    public String documentation;

    public KeywordData(){
        this.arguments = new  ArrayList<>();
        this.variables =  new HashMap<>();
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(cleanArgument(this.name));

        for(String argument : arguments) {
            stringBuilder.append("\t");
            stringBuilder.append(cleanArgument(argument));
        }

        return stringBuilder.toString();
    }

    public String getLabel() {
        return toString();
    }

    public boolean isSame(TreeNodeData other) {
        if(! this.getClass().equals(other.getClass())){
            return false;
        }

        return this.file.equals(((KeywordData)other).file)
                && this.name.equals(((KeywordData)other).name)
                && this.arguments.size() == ((KeywordData)other).arguments.size();
    }

    public boolean isValid() {
        //TODO: Create better method to define valid nodes
        return !this.name.equalsIgnoreCase("None") && !this.name.isEmpty();
    }

    public String getCleanName() {
        return cleanArgument(this.name);
    }

    public List<String> getCleanArguments() {
        List<String> cleanArguments = new ArrayList<String>();

        for(String argument : arguments) {
            cleanArguments.add(cleanArgument(argument));
        }

        return cleanArguments;
    }

    private String cleanArgument(String argument) {
        if (!Argument.hasVariable(argument)) {
            return argument;
        }

        List<String> variables;

         do {
             variables = Argument.findVariables(argument);

            for(String variable : variables) {
                argument = argument.replace(variable, resolveVariable(variable));
            }

         } while (!variables.isEmpty());

         return argument;
    }

    private String resolveVariable(String variable) {
        List<String> values = variables.get(variable);

        String returnValue;

        if(values == null || values.isEmpty()) {
            returnValue = variable.replace("${", "[");
            returnValue = returnValue.replace("}", "]");
        }
        else if (values.size() == 1) {
            String value = values.get(0);

            if(Argument.hasVariable(value)) {
                value = cleanArgument(value);
            }

            returnValue = value;
        }
        else
        {
            StringBuilder stringBuilder = new StringBuilder();

            for(String value : values) {
                stringBuilder.append("\t");
                stringBuilder.append(resolveVariable(value));
            }

            returnValue = stringBuilder.toString().trim();
        }

        return returnValue;
    }

}
