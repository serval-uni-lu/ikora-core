package org.ikora.analytics;

import org.ikora.model.*;

import java.util.ArrayList;
import java.util.List;

public class Action implements Differentiable {
    public enum Type{
        ADD_USER_KEYWORD,
        REMOVE_USER_KEYWORD,
        CHANGE_NAME,

        ADD_TEST_CASE,
        REMOVE_TEST_CASE,

        CHANGE_STEP_TYPE,
        ADD_STEP,
        REMOVE_STEP,
        CHANGE_STEP,
        CHANGE_STEP_ARGUMENTS,

        CHANGE_STEP_EXPRESSION,
        CHANGE_STEP_RETURN_VALUES,

        CHANGE_FOR_LOOP_CONDITION,
        CHANGE_FOR_LOOP_BODY,

        ADD_VARIABLE,
        REMOVE_VARIABLE,
        CHANGE_VARIABLE_DEFINITION,

        ADD_SEQUENCE,
        REMOVE_SEQUENCE,

        ADD_DOCUMENTATION,
        REMOVE_DOCUMENTATION,
        CHANGE_DOCUMENTATION,

        INVALID
    }

    private final Type type;
    private final Differentiable left;
    private final Differentiable right;

    private Action(Type type, Differentiable left, Differentiable right){
        this.type = type;
        this.left = left;
        this.right = right;
    }

    public Type getType() {
        return type;
    }

    public Differentiable getLeft() {
        return left;
    }

    public Differentiable getRight() {
        return right;
    }

    public Differentiable getValue() {
        if(this.left != null){
            return this.left;
        }

        return this.right;
    }

    @Override
    public double distance(Differentiable other) {
        if(other == null){
            return 1.0;
        }

        if(other.getClass() != this.getClass()){
            return 1.0;
        }

        if(this.type == ((Action)other).type && this.getValue().getName().equals(((Action)other).getValue().getName())){
            return 0.0;
        }

        return 1.0;
    }

    @Override
    public List<Action> differences(Differentiable other) {
        List<Action> differences = new ArrayList<>();

        if(other == null || other.getClass() != this.getClass()){
            return differences;
        }

        if(this.type != ((Action)other).type
        && this.getValue().getName().equals(((Action)other).getValue().getName())){
            differences.add(Action.changeStep(this, other));
        }

        return differences;
    }

    @Override
    public String getName() {
        return this.getType().name();
    }

    public static <T> Action addElement(Class<T> type, Differentiable element) {
        if(UserKeyword.class.isAssignableFrom(type)){
            return new Action(Type.ADD_USER_KEYWORD, null, element);
        }
        else if(TestCase.class.isAssignableFrom(type)){
            return new Action(Type.ADD_TEST_CASE, null, element);
        }
        else if(Variable.class.isAssignableFrom(type)){
            return new Action(Type.ADD_VARIABLE, null, element);
        }
        else if(Step.class.isAssignableFrom(type)){
            return new Action(Type.ADD_STEP, null, element);
        }
        else if(Sequence.class.isAssignableFrom(type)){
            return new Action(Type.ADD_SEQUENCE, null, element);
        }

        return new Action(Type.INVALID, null, element);
    }

    public static <T> Action removeElement(Class<T> type, Differentiable element){
        if(UserKeyword.class.isAssignableFrom(type)){
            return new Action(Type.REMOVE_USER_KEYWORD, element, null);
        }
        else if(TestCase.class.isAssignableFrom(type)){
            return new Action(Type.REMOVE_TEST_CASE, element, null);
        }
        else if(Variable.class.isAssignableFrom(type)){
            return new Action(Type.REMOVE_VARIABLE, element, null);
        }
        else if(Step.class.isAssignableFrom(type)){
            return new Action(Type.REMOVE_STEP, element, null);
        }
        else if(Sequence.class.isAssignableFrom(type)){
            return new Action(Type.REMOVE_SEQUENCE, element, null);
        }

        return new Action(Type.INVALID, element, null);
    }

    public static Action changeName(Differentiable left, Differentiable right){
        return new Action(Type.CHANGE_NAME, left, right);
    }

    public static Action changeStep(Differentiable left, Differentiable right){
        return new Action(Type.CHANGE_STEP, left, right);
    }

    public static Action changeStepType(Differentiable left, Differentiable right){
        return new Action(Type.CHANGE_STEP_TYPE, left, right);
    }

    public static Action changeStepName(Differentiable left, Differentiable right){
        return new Action(Type.CHANGE_STEP, left, right);
    }

    public static Action changeStepArguments(Differentiable left, Differentiable right){
        return new Action(Type.CHANGE_STEP_ARGUMENTS, left, right);
    }

    public static Action changeStepExpression(Differentiable left, Differentiable right){
        return new Action(Type.CHANGE_STEP_EXPRESSION, left, right);
    }

    public static Action changeStepReturnValues(Differentiable left, Differentiable right){
        return new Action(Type.CHANGE_STEP_RETURN_VALUES, left, right);
    }

    public static Action changeForLoopCondition(Differentiable left, Differentiable right) {
        return new Action(Type.CHANGE_FOR_LOOP_CONDITION, left, right);
    }

    public static Action changeForLoopBody(Differentiable left, Differentiable right) {
        return new Action(Type.CHANGE_FOR_LOOP_BODY, left, right);
    }

    public static Action changeVariableDefinition(Differentiable left, Differentiable right) {
        return new Action(Type.CHANGE_VARIABLE_DEFINITION, left, right);
    }

    public static Action addDocumentation(Differentiable left, Differentiable right) {
        return new Action(Type.ADD_DOCUMENTATION, left, right);
    }

    public static Action removeDocumentation(Differentiable left, Differentiable right) {
        return new Action(Type.REMOVE_DOCUMENTATION, left, right);
    }

    public static Action changeDocumentation(Differentiable left, Differentiable right) {
        return new Action(Type.CHANGE_DOCUMENTATION, left, right);
    }

    public static Action invalid(Differentiable left, Differentiable right) {
        return new Action(Type.INVALID, left, right);
    }
}
