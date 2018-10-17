package lu.uni.serval.robotframework.model;

import java.util.ArrayList;
import java.util.List;

public class UserKeyword extends KeywordDefinition {
    private List<String> parameters;
    private ElementTable<Variable> localVariables;

    public UserKeyword() {
        parameters = new ArrayList<>();
        localVariables = new ElementTable<>();
    }

    @Override
    public void setName(String name) {
        super.setName(name);

        for(String argument: getNameAsArgument().findVariables()){
            addParameter(argument);
        }
    }

    @Override
    public void addStep(Step step){
        super.addStep(step);

        if(Assignment.class.isAssignableFrom(step.getClass())){
            for (Variable variable: ((Assignment)step).getReturnValues()){
                localVariables.add(variable);
            }
        }
    }

    @Override
    public Argument.Type[] getArgumentTypes() {
        Argument.Type[] types = new Argument.Type[parameters.size()];

        for(int i = 0; i < types.length; ++i){
            types[i] = Argument.Type.String;
        }

        return types;
    }

    @Override
    public int getMaxArgument(){
        return parameters.size();
    }

    public void addParameter(String parameter){
        parameters.add(parameter);

        Variable variable = new Variable();
        variable.setName(parameter);

        localVariables.add(variable);
    }

    public List<String> getParameters() {
        return parameters;
    }

    public Variable findLocalVariable(String name) {
        return localVariables.findElement(name);
    }
}
