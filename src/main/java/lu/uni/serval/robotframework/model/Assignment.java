package lu.uni.serval.robotframework.model;

import lu.uni.serval.robotframework.runner.Runtime;

import java.util.ArrayList;
import java.util.List;

public class Assignment extends Step {
    private List<Variable> returnValues;
    private Step expression;

    public Assignment(){
        returnValues = new ArrayList<>();
    }

    @Override
    public void setName(String name) {
        super.setName(name);
        String[] tokens = getName().toString().split("=");

        if(tokens.length != 2){
            return;
        }

        Argument left = new Argument(tokens[0]);
        String right = tokens[1];

        for(String argument: left.findVariables()){
            Variable variable = new Variable();
            variable.setName(argument);

            returnValues.add(variable);
        }

        expression = new KeywordCall();
        expression.setName(right);
    }

    @Override
    public List<Argument> getParameters() {
        return expression.getParameters();
    }

    @Override
    public void execute(Runtime runtime) {

    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof Assignment)){
            return false;
        }

        if(!super.equals(other)) {
            return false;
        }

        Assignment assignment = (Assignment)other;

        boolean same = this.expression.equals(assignment.expression);

        for(int i = 0; same && i < this.returnValues.size(); ++i) {
            same &= this.returnValues.get(i).getName().equalsIgnoreCase(assignment.returnValues.get(i).getName());
        }

        return  same;
    }

}
