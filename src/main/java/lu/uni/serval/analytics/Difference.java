package lu.uni.serval.analytics;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lu.uni.serval.robotframework.model.*;
import lu.uni.serval.utils.LevenshteinDistance;

import java.util.ArrayList;
import java.util.List;

@JsonSerialize(using = DifferenceSerializer.class)
public class Difference {

    private KeywordDefinition left;
    private KeywordDefinition right;
    private List<Action> actions;

    private Difference(KeywordDefinition left, KeywordDefinition right){
        this.left = left;
        this.right = right;

        this.actions = new ArrayList<>();
    }

    public boolean isEmpty(){
        return actions.isEmpty();
    }

    public KeywordDefinition getLeft(){
        return left;
    }

    public KeywordDefinition getRight(){
        return right;
    }

    public List<Action> getActions() {
        return actions;
    }

    public static Difference of(KeywordDefinition before, KeywordDefinition after) {
        Difference difference = new Difference(before, after);

        if(before == after){
            return difference;
        }

        if(before == null){
            difference.actions.add(Action.addUserKeyword());
            return difference;
        }

        if(after == null){
            difference.actions.add(Action.removeUserKeyword());
            return difference;
        }

        if(!before.getName().toString().equalsIgnoreCase(after.getName().toString())){
            difference.actions.add(Action.changeName());
        }

        difference.extractStepDifferences();

        return difference;
    }

    private void extractStepDifferences(){
        double[][] distances = LevenshteinDistance.distanceMatrix(left.getSteps(), right.getSteps());
        computeEditMapping(distances);
    }

    private void computeEditMapping(double[][] distances) {
        int xPosition = left.getSteps().size();
        int yPosition = right.getSteps().size();

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
        Step leftStep = (Step)left.getStep(leftPosition);
        Step rightStep = (Step)right.getStep(rightPosition);

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
        KeywordCall leftStep = (KeywordCall)left.getStep(leftPosition);
        KeywordCall rightStep = (KeywordCall)right.getStep(rightPosition);

        if(!leftStep.getName().toString().equalsIgnoreCase(rightStep.toString())){
            actions.add(Action.changeStepName(leftPosition, rightPosition));
        }

        if(LevenshteinDistance.index(leftStep.getParameters(), rightStep.getParameters()) > 0){
            actions.add(Action.changeStepArguments(leftPosition, rightPosition));
        }
    }

    private void setAssignmentChanges(int leftPosition, int rightPosition) {
        Assignment leftStep = (Assignment)left.getStep(leftPosition);
        Assignment rightStep = (Assignment)right.getStep(rightPosition);

        if(leftStep.getExpression().difference(rightStep.getExpression()) > 0){
            actions.add(Action.changeStepExpression(leftPosition, rightPosition));
        }

        if(LevenshteinDistance.index(leftStep.getReturnValues(), rightStep.getReturnValues()) > 0){
            actions.add(Action.changeStepReturnValues(leftPosition, rightPosition));
        }
    }

    private void setForLoopChanges(int leftPosition, int rightPosition) {
        ForLoop leftStep = (ForLoop)left.getStep(leftPosition);
        ForLoop rightStep = (ForLoop)right.getStep(rightPosition);

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

    private int getNodeHash(int hash, KeywordDefinition keyword){
        if(keyword == null){
            hash = 31 * hash;
        }
        else {
            hash = 31 * hash + (keyword.getFile() == null ? 0 : keyword.getFile().hashCode());
            hash = 31 * hash + (keyword.getName().toString() == null ? 0 : keyword.getName().toString().hashCode());
        }

        return hash;
    }
}
