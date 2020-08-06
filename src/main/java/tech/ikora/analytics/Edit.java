package tech.ikora.analytics;

import tech.ikora.utils.DifferentiableString;
import tech.ikora.model.*;

import java.util.ArrayList;
import java.util.List;

public class Edit implements Differentiable {
    public enum Type{
        ADD_USER_KEYWORD,
        REMOVE_USER_KEYWORD,
        CHANGE_NAME,

        ADD_TEST_CASE,
        REMOVE_TEST_CASE,

        CHANGE_TYPE,
        ADD_STEP,
        REMOVE_STEP,
        CHANGE_STEP,

        ADD_STEP_ARGUMENT,
        REMOVE_STEP_ARGUMENT,
        CHANGE_VALUE_NAME,
        CHANGE_VALUE_TYPE,

        ADD_VARIABLE,
        REMOVE_VARIABLE,
        CHANGE_VARIABLE_DEFINITION,

        ADD_SEQUENCE,
        REMOVE_SEQUENCE,

        ADD_STRING,
        REMOVE_STRING,

        ADD_DOCUMENTATION,
        REMOVE_DOCUMENTATION,
        CHANGE_DOCUMENTATION,

        ADD_NODE,
        REMOVE_NODE,

        INVALID
    }

    private final Type type;
    private final Differentiable left;
    private final Differentiable right;

    private Edit(Type type, Differentiable left, Differentiable right){
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

        if(this.type == ((Edit)other).type && this.getValue().toString().equals(((Edit)other).getValue().toString())){
            return 0.0;
        }

        return 1.0;
    }

    @Override
    public List<Edit> differences(Differentiable other) {
        List<Edit> differences = new ArrayList<>();

        if(other == null || other.getClass() != this.getClass()){
            return differences;
        }

        if(this.type != ((Edit)other).type
        && this.getValue().toString().equals(((Edit)other).getValue().toString())){
            differences.add(Edit.changeStep(this, other));
        }

        return differences;
    }

    public static <T> Edit addElement(Class<T> type, Differentiable element) {
        if(UserKeyword.class.isAssignableFrom(type)){
            return new Edit(Type.ADD_USER_KEYWORD, null, element);
        }
        else if(TestCase.class.isAssignableFrom(type)){
            return new Edit(Type.ADD_TEST_CASE, null, element);
        }
        else if(Variable.class.isAssignableFrom(type)){
            return new Edit(Type.ADD_VARIABLE, null, element);
        }
        else if(Step.class.isAssignableFrom(type)){
            return new Edit(Type.ADD_STEP, null, element);
        }
        else if(Argument.class.isAssignableFrom(type)){
            return new Edit(Type.ADD_STEP_ARGUMENT, null, element);
        }
        else if(Sequence.class.isAssignableFrom(type)){
            return new Edit(Type.ADD_SEQUENCE, null, element);
        }
        else if(DifferentiableString.class.isAssignableFrom(type)){
            return new Edit(Type.ADD_STRING, null, element);
        }

        return new Edit(Type.ADD_NODE, null, element);
    }

    public static <T> Edit removeElement(Class<T> type, Differentiable element){
        if(UserKeyword.class.isAssignableFrom(type)){
            return new Edit(Type.REMOVE_USER_KEYWORD, element, null);
        }
        else if(TestCase.class.isAssignableFrom(type)){
            return new Edit(Type.REMOVE_TEST_CASE, element, null);
        }
        else if(Variable.class.isAssignableFrom(type)){
            return new Edit(Type.REMOVE_VARIABLE, element, null);
        }
        else if(Step.class.isAssignableFrom(type)){
            return new Edit(Type.REMOVE_STEP, element, null);
        }
        else if(Argument.class.isAssignableFrom(type)){
            return new Edit(Type.REMOVE_STEP_ARGUMENT, element, null);
        }
        else if(Sequence.class.isAssignableFrom(type)){
            return new Edit(Type.REMOVE_SEQUENCE, element, null);
        }
        else if(DifferentiableString.class.isAssignableFrom(type)){
            return new Edit(Type.REMOVE_STRING, element, null);
        }

        return new Edit(Type.INVALID, element, null);
    }

    public static Edit changeName(Differentiable left, Differentiable right){
        return new Edit(Type.CHANGE_NAME, left, right);
    }

    public static Edit changeStep(Differentiable left, Differentiable right){
        return new Edit(Type.CHANGE_STEP, left, right);
    }

    public static Edit changeType(Differentiable left, Differentiable right){
        return new Edit(Type.CHANGE_TYPE, left, right);
    }

    public static Edit changeStepName(Differentiable left, Differentiable right){
        return new Edit(Type.CHANGE_STEP, left, right);
    }

    public static Edit changeValueName(Differentiable left, Differentiable right){
        return new Edit(Type.CHANGE_VALUE_NAME, left, right);
    }

    public static Edit changeValueType(Differentiable left, Differentiable right){
        return new Edit(Type.CHANGE_VALUE_TYPE, left, right);
    }

    public static Edit changeVariableDefinition(Differentiable left, Differentiable right) {
        return new Edit(Type.CHANGE_VARIABLE_DEFINITION, left, right);
    }

    public static Edit addDocumentation(Differentiable left, Differentiable right) {
        return new Edit(Type.ADD_DOCUMENTATION, left, right);
    }

    public static Edit removeDocumentation(Differentiable left, Differentiable right) {
        return new Edit(Type.REMOVE_DOCUMENTATION, left, right);
    }

    public static Edit changeDocumentation(Differentiable left, Differentiable right) {
        return new Edit(Type.CHANGE_DOCUMENTATION, left, right);
    }

    public static Edit invalid(Differentiable left, Differentiable right) {
        return new Edit(Type.INVALID, left, right);
    }
}
