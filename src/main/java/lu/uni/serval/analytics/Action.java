package lu.uni.serval.analytics;

import lu.uni.serval.robotframework.model.TestCase;
import lu.uni.serval.robotframework.model.UserKeyword;
import lu.uni.serval.robotframework.model.Variable;
import lu.uni.serval.utils.Differentiable;

public class Action {
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

    public static <T> Action addElement(Class<T> type) {
        if(type == UserKeyword.class){
            return new Action(Type.ADD_USER_KEYWORD, null, null);
        }
        else if(type == TestCase.class){
            return new Action(Type.ADD_TEST_CASE, null, null);
        }
        else if(type == Variable.class){
            return new Action(Type.ADD_VARIABLE, null, null);
        }

        return new Action(Type.INVALID, null, null);
    }

    public static <T> Action removeElement(Class<T> type){
        if(type == UserKeyword.class){
            return new Action(Type.REMOVE_USER_KEYWORD, null, null);
        }
        else if(type == TestCase.class){
            return new Action(Type.REMOVE_TEST_CASE, null, null);
        }
        else if(type == Variable.class){
            return new Action(Type.REMOVE_VARIABLE, null, null);
        }

        return new Action(Type.INVALID, null, null);
    }

    public static Action changeName(){
        return new Action(Type.CHANGE_NAME, null, null);
    }

    public static Action changeStepType(Differentiable left, Differentiable right){
        return new Action(Type.CHANGE_STEP_TYPE, left, right);
    }

    public static Action removeStep(Differentiable left){
        return new Action(Type.REMOVE_STEP, left, null);
    }

    public static Action insertStep(Differentiable right){
        return new Action(Type.ADD_STEP, null, right);
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

    public static Action changeVariableDefinition() {
        return new Action(Type.CHANGE_VARIABLE_DEFINITION, null, null);
    }
}
