package lu.uni.serval.utils;

import lu.uni.serval.robotframework.model.Argument;
import lu.uni.serval.utils.tree.TreeNodeData;

import java.util.*;

public class KeywordData implements TreeNodeData {


    public String name;
    public String type;
    public List<String> arguments;
    private Map<String, List<String>> variables;
    public String file;
    public String documentation;
    public String library;

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
        List<String> cleanArguments = new ArrayList<>();

        for(String argument : arguments) {
            cleanArguments.add(cleanArgument(argument));
        }

        return cleanArguments;
    }

    public void addVariable(String variable, List<String> values){
        String safeVariable = variable.replace("${", "[");
        safeVariable = safeVariable.replace("}", "]");

        Collections.replaceAll(values, variable, safeVariable);

        this.variables.put(variable, values);
    }

    private String cleanArgument(String argument) {
        return cleanArgument(argument, 0);
    }

    private String cleanArgument(String argument, int iteration) {
        if (!Argument.hasVariable(argument)) {
            return argument;
        }

        List<String> variables;

         do {

             variables = Argument.findVariables(argument);

            for(String variable : variables) {
                argument = argument.replace(variable, resolveVariable(variable, ++iteration));
            }
         } while (!variables.isEmpty());

         return argument;
    }

    private String resolveVariable(String variable, int iteration) {
        List<String> values = variables.get(variable);

        String returnValue;
        if(values == null || values.isEmpty()) {
            returnValue = variable.replace("${", "[");
            returnValue = returnValue.replace("}", "]");
        }
        else if (values.size() == 1) {
            String value = values.get(0);

            if(Argument.hasVariable(value)) {
                value = cleanArgument(value, iteration);
            }

            returnValue = value;
        }
        else
        {
            StringBuilder stringBuilder = new StringBuilder();

            for(String value : values) {
                stringBuilder.append("\t");
                stringBuilder.append(resolveVariable(value, iteration));
            }

            returnValue = stringBuilder.toString().trim();
        }

        return returnValue;
    }

}
