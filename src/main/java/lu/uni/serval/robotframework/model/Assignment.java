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

    public void addReturnValue(String returnValue){
        Variable variable = new Variable();
        variable.setName(returnValue);

        returnValues.add(variable);
    }

    public void setExpression(KeywordCall call) {
        expression = call;
    }

    @Override
    public Keyword getStep(int position) {
        return null;
    }

    @Override
    public List<Argument> getParameters() {
        if(expression == null){
            return new ArrayList<>();
        }

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
    public double difference(Differentiable<Step> other) {
        if(!(other instanceof Assignment)){
            return 1;
        }

        Assignment assignment = (Assignment)other;

        double expressionIndex = expression.difference(assignment.expression);
        double returnValuesIndex = LevenshteinDistance.index(returnValues, assignment.returnValues);

        return (0.5 * expressionIndex) + (0.5 * returnValuesIndex);
    }
}
