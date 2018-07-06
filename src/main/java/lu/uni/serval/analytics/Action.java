package lu.uni.serval.analytics;

public class Action {
    public enum Type{
        CHANGE_NAME, ADD_STEP, REMOVE_STEP, CHANGE_STEP_ARGUMENTS
    }

    private final Type type;
    private final int before;
    private final int after;

    private Action(Type type, int before, int after){
        this.type = type;
        this.before = before;
        this.after = after;
    }

    public Type getType() {
        return type;
    }

    public int getBefore() {
        return before;
    }

    public int getAfter() {
        return after;
    }

    public static Action changeName(int before, int after){
        return new Action(Type.CHANGE_NAME, before, after);
    }

    public static Action removeStep(int before){
        return new Action(Type.REMOVE_STEP, before, -1);
    }

    public static Action insertStep(int after){
        return new Action(Type.ADD_STEP, -1, after);
    }

    public static Action changeStepArguments(int before, int after){
        return new Action(Type.CHANGE_STEP_ARGUMENTS, before, after);
    }
}
