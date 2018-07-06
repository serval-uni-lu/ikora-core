package lu.uni.serval.analytics;

public class Action {
    public enum Type{
        CHANGE_NAME,

        CHANGE_STEP_TYPE,
        ADD_STEP,
        REMOVE_STEP,

        CHANGE_STEP_NAME,
        CHANGE_STEP_ARGUMENTS,

        CHANGE_STEP_EXPRESSION,
        CHANGE_STEP_RETURN_VALUES
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
        return new Action(Type.CHANGE_STEP_NAME, left, right);
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
}
