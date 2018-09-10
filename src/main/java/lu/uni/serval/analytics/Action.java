package lu.uni.serval.analytics;

import lu.uni.serval.robotframework.model.TestCase;
import lu.uni.serval.robotframework.model.UserKeyword;

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

        INVALID
    }

    private final Type type;
    private final int left;
    private final int right;

    private Action(Type type, int left, int right){
        this.type = type;
        this.left = left;
        this.right = right;
    }

    public Type getType() {
        return type;
    }

    public int getLeft() {
        return left;
    }

    public int getRight() {
        return right;
    }

    public static <T> Action addElement(Class<T> type) {
        if(type == UserKeyword.class){
            return new Action(Type.ADD_USER_KEYWORD, -1, -1);
        }
        else if(type == TestCase.class){
            return new Action(Type.ADD_TEST_CASE, -1, -1);
        }

        return new Action(Type.INVALID, -1, -1);
    }

    public static <T> Action removeElement(Class<T> type){
        if(type == UserKeyword.class){
            return new Action(Type.REMOVE_USER_KEYWORD, -1, -1);
        }
        else if(type == TestCase.class){
            return new Action(Type.REMOVE_TEST_CASE, -1, -1);
        }

        return new Action(Type.INVALID, -1, -1);
    }

    public static Action changeName(){
        return new Action(Type.CHANGE_NAME, -1, -1);
    }

    public static Action changeStepType(int left, int right){
        return new Action(Type.CHANGE_STEP_TYPE, left, right);
    }

    public static Action removeStep(int left){
        return new Action(Type.REMOVE_STEP, left, -1);
    }

    public static Action insertStep(int right){
        return new Action(Type.ADD_STEP, -1, right);
    }

    public static Action changeStepName(int left, int right){
        return new Action(Type.CHANGE_STEP, left, right);
    }

    public static Action changeStepArguments(int left, int right){
        return new Action(Type.CHANGE_STEP_ARGUMENTS, left, right);
    }

    public static Action changeStepExpression(int left, int right){
        return new Action(Type.CHANGE_STEP_EXPRESSION, left, right);
    }

    public static Action changeStepReturnValues(int left, int right){
        return new Action(Type.CHANGE_STEP_RETURN_VALUES, left, right);
    }

    public static Action changeForLoopCondition(int left, int right) {
        return new Action(Type.CHANGE_FOR_LOOP_CONDITION, left, right);
    }

    public static Action changeForLoopBody(int left, int right) {
        return new Action(Type.CHANGE_FOR_LOOP_BODY, left, right);
    }
}
