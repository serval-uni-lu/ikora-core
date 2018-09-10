package lu.uni.serval.analytics;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lu.uni.serval.robotframework.model.*;
import lu.uni.serval.utils.Differentiable;
import lu.uni.serval.utils.LevenshteinDistance;

import java.util.*;

@JsonSerialize(using = DifferenceSerializer.class)
public class Difference {

    private Differentiable left;
    private Differentiable right;
    private List<Action> actions;

    private Difference(Differentiable left, Differentiable right){
        this.left = left;
        this.right = right;

        this.actions = new ArrayList<>();
    }

    public boolean isEmpty(){
        return actions.isEmpty();
    }

    public boolean isEmpty(Action.Type[] ignore){
        if(actions.isEmpty()){
            return true;
        }

        if(ignore.length == 0){
            return isEmpty();
        }

        Set<Action.Type> ignoreSet = new HashSet<>(Arrays.asList(ignore));

        for(Action action: actions){
            if (!ignoreSet.contains(action.getType())){
                return false;
            }
        }

        return true;
    }

    public Differentiable getLeft(){
        return left;
    }

    public Differentiable getRight(){
        return right;
    }

    public List<Action> getActions() {
        return actions;
    }

    public static Difference of(Differentiable before, Differentiable after) {
        Difference difference = new Difference(before, after);

        if(before == after){
            return difference;
        }

        if(before == null){
            difference.actions.add(Action.addElement(after.getClass()));
            return difference;
        }

        if(after == null){
            difference.actions.add(Action.removeElement(before.getClass()));
            return difference;
        }

        difference.extractNameDifference();
        difference.extractStepDifferences();

        return difference;
    }

    private void extractNameDifference(){
        if(left == null || right == null){
            return;
        }

        if(!(left instanceof KeywordDefinition)){
            return;
        }

        KeywordDefinition before = (KeywordDefinition)left;
        KeywordDefinition after = (KeywordDefinition)right;

        if(!before.getName().toString().equalsIgnoreCase(after.getName().toString())){
            actions.add(Action.changeName());
        }
    }

    private void extractStepDifferences(){
        if(left == null || right == null){
            return;
        }

        if(!(left instanceof KeywordDefinition)){
            return;
        }

        KeywordDefinition before = (KeywordDefinition)left;
        KeywordDefinition after = (KeywordDefinition)right;

        double[][] distances = LevenshteinDistance.distanceMatrix(before.getSteps(), after.getSteps());
        computeEditMapping(distances);
    }

    private void computeEditMapping(double[][] distances) {
        KeywordDefinition before = (KeywordDefinition)left;
        KeywordDefinition after = (KeywordDefinition)right;

        int xPosition = before.getSteps().size();
        int yPosition = after.getSteps().size();

        double value = distances[xPosition][yPosition];
        double initialValue = value;

        while(value != 0){
            double substitution = xPosition > 0 && yPosition > 0 ? distances[xPosition - 1][yPosition - 1] : initialValue;
            double addition = yPosition > 0 ? distances[xPosition][yPosition - 1] : initialValue;
            double subtraction = xPosition > 0 ? distances[xPosition - 1][yPosition] : initialValue;

            if(substitution < subtraction && substitution < addition){
                if(value > substitution){
                    setStepChanges(xPosition - 1, yPosition - 1);
                }

                value = substitution;
                xPosition -= 1;
                yPosition -= 1;
            }
            else if (subtraction < addition){
                actions.add(Action.removeStep(xPosition - 1));

                value = subtraction;
                xPosition -= 1;
            }
            else{
                actions.add(Action.insertStep(yPosition - 1));

                value = addition;
                yPosition -= 1;
            }
        }
    }

    private void setStepChanges(int leftPosition, int rightPosition) {
        KeywordDefinition before = (KeywordDefinition)left;
        KeywordDefinition after = (KeywordDefinition)right;

        Step leftStep = (Step)before.getStep(leftPosition);
        Step rightStep = (Step)after.getStep(rightPosition);

        if(leftStep.getClass() != rightStep.getClass()){
            actions.add(Action.changeStepType(leftPosition, rightPosition));
        }
        else{
            if(leftStep instanceof KeywordCall){
                setKeywordCallChanges(leftPosition, rightPosition);
            }
            else if(leftStep instanceof Assignment){
                setAssignmentChanges(leftPosition, rightPosition);
            }
            else if(leftStep instanceof ForLoop){
                setForLoopChanges(leftPosition, rightPosition);
            }
        }
    }

    private void setKeywordCallChanges(int leftPosition, int rightPosition) {
        KeywordDefinition before = (KeywordDefinition)left;
        KeywordDefinition after = (KeywordDefinition)right;

        KeywordCall leftStep = (KeywordCall)before.getStep(leftPosition);
        KeywordCall rightStep = (KeywordCall)after.getStep(rightPosition);

        if(!leftStep.getName().toString().equalsIgnoreCase(rightStep.toString())){
            actions.add(Action.changeStepName(leftPosition, rightPosition));
        }

        if(LevenshteinDistance.index(leftStep.getParameters(), rightStep.getParameters()) > 0){
            actions.add(Action.changeStepArguments(leftPosition, rightPosition));
        }
    }

    private void setAssignmentChanges(int leftPosition, int rightPosition) {
        KeywordDefinition before = (KeywordDefinition)left;
        KeywordDefinition after = (KeywordDefinition)right;

        Assignment leftStep = (Assignment)before.getStep(leftPosition);
        Assignment rightStep = (Assignment)after.getStep(rightPosition);

        if(leftStep.getExpression().distance(rightStep.getExpression()) > 0){
            actions.add(Action.changeStepExpression(leftPosition, rightPosition));
        }

        if(LevenshteinDistance.index(leftStep.getReturnValues(), rightStep.getReturnValues()) > 0){
            actions.add(Action.changeStepReturnValues(leftPosition, rightPosition));
        }
    }

    private void setForLoopChanges(int leftPosition, int rightPosition) {
        KeywordDefinition before = (KeywordDefinition)left;
        KeywordDefinition after = (KeywordDefinition)right;

        ForLoop leftStep = (ForLoop)before.getStep(leftPosition);
        ForLoop rightStep = (ForLoop)after.getStep(rightPosition);

        if(LevenshteinDistance.stringIndex(leftStep.getName().toString(), rightStep.getName().toString()) > 0){
            actions.add(Action.changeForLoopCondition(leftPosition, rightPosition));
        }

        if(LevenshteinDistance.index(leftStep.getSteps(), rightStep.getSteps()) > 0){
            actions.add(Action.changeForLoopBody(leftPosition, rightPosition));
        }
    }

    @Override
    public boolean equals(Object other) {
        if(this == other){
            return true;
        }

        if(other == null){
            return false;
        }

        if(this.getClass() != other.getClass()){
            return false;
        }

        Difference difference = (Difference)other;

        return this.left == difference.left
                && this.right == difference.right;
    }

    @Override
    public int hashCode(){
        int hash = 7;
        hash = getNodeHash(hash, left);
        hash = getNodeHash(hash, right);

        return hash;
    }

    private int getNodeHash(int hash, Differentiable element){
        if(element == null){
            hash = 31 * hash;
        }
        else if(element instanceof KeywordDefinition){
            KeywordDefinition keyword = (KeywordDefinition)element;

            hash = 31 * hash + (keyword.getFile() == null ? 0 : keyword.getFile().hashCode());
            hash = 31 * hash + (keyword.getName().toString() == null ? 0 : keyword.getName().toString().hashCode());
        }
        else{
            hash = element.hashCode();
        }

        return hash;
    }
}
