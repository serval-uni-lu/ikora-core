package lu.uni.serval.robotframework.model;

import lu.uni.serval.robotframework.runner.Runtime;
import lu.uni.serval.utils.Differentiable;
import lu.uni.serval.utils.LevenshteinDistance;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class Assignment extends Step {
    final static Logger logger = Logger.getLogger(Assignment.class);

    private List<Variable> returnValues;

    private Step expression;

    public Assignment(){
        returnValues = new ArrayList<>();
    }

    public Step getExpression() {
        return expression;
    }

    public List<Variable> getReturnValues() {
        return returnValues;
    }

    @Override
    public Keyword getStep(int position) {
        return null;
    }

    @Override
    public void setName(String name) {
        super.setName(name);
        String[] tokens = name.split("=", 1);

        if(tokens.length != 2){
            logger.error("trying to set invalid assignment name: " + name);
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

    @Override
    public double indexTo(Differentiable<Step> other) {
        if(!(other instanceof Assignment)){
            return 1;
        }

        Assignment assignment = (Assignment)other;

        double expressionIndex = expression.indexTo(assignment.expression);
        double returnValuesIndex = LevenshteinDistance.index(returnValues, assignment.returnValues);

        return (0.5 * expressionIndex) + (0.5 * returnValuesIndex);
    }
}
