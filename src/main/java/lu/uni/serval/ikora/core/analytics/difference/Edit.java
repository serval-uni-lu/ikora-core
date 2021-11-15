package lu.uni.serval.ikora.core.analytics.difference;

import lu.uni.serval.ikora.core.model.*;

public class Edit {
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

        ADD_DOCUMENTATION,
        REMOVE_DOCUMENTATION,
        CHANGE_DOCUMENTATION,

        ADD_NODE,
        REMOVE_NODE,

        INVALID
    }

    private final Type type;
    private final SourceNode left;
    private final SourceNode right;

    private Edit(Type type, SourceNode left, SourceNode right){
        this.type = type;
        this.left = left;
        this.right = right;
    }

    public Type getType() {
        return type;
    }

    public SourceNode getLeft() {
        return left;
    }

    public SourceNode getRight() {
        return right;
    }

    public SourceNode getValue() {
        if(this.left != null){
            return this.left;
        }

        return this.right;
    }

    public static <T> Edit addElement(Class<T> type, SourceNode node) {
        if(UserKeyword.class.isAssignableFrom(type)){
            return new Edit(Type.ADD_USER_KEYWORD, null, node);
        }
        else if(TestCase.class.isAssignableFrom(type)){
            return new Edit(Type.ADD_TEST_CASE, null, node);
        }
        else if(Variable.class.isAssignableFrom(type)){
            return new Edit(Type.ADD_VARIABLE, null, node);
        }
        else if(Step.class.isAssignableFrom(type)){
            return new Edit(Type.ADD_STEP, null, node);
        }
        else if(Argument.class.isAssignableFrom(type)){
            return new Edit(Type.ADD_STEP_ARGUMENT, null, node);
        }
        else if(Sequence.class.isAssignableFrom(type)){
            return new Edit(Type.ADD_SEQUENCE, null, node);
        }

        return new Edit(Type.ADD_NODE, null, node);
    }

    public static <T> Edit removeElement(Class<T> type, SourceNode node){
        if(UserKeyword.class.isAssignableFrom(type)){
            return new Edit(Type.REMOVE_USER_KEYWORD, node, null);
        }
        else if(TestCase.class.isAssignableFrom(type)){
            return new Edit(Type.REMOVE_TEST_CASE, node, null);
        }
        else if(Variable.class.isAssignableFrom(type)){
            return new Edit(Type.REMOVE_VARIABLE, node, null);
        }
        else if(Step.class.isAssignableFrom(type)){
            return new Edit(Type.REMOVE_STEP, node, null);
        }
        else if(Argument.class.isAssignableFrom(type)){
            return new Edit(Type.REMOVE_STEP_ARGUMENT, node, null);
        }
        else if(Sequence.class.isAssignableFrom(type)){
            return new Edit(Type.REMOVE_SEQUENCE, node, null);
        }

        return new Edit(Type.INVALID, node, null);
    }

    public static Edit changeName(SourceNode left, SourceNode right){
        return new Edit(Type.CHANGE_NAME, left, right);
    }

    public static Edit changeStep(SourceNode left, SourceNode right){
        return new Edit(Type.CHANGE_STEP, left, right);
    }

    public static Edit changeType(SourceNode left, SourceNode right){
        return new Edit(Type.CHANGE_TYPE, left, right);
    }

    public static Edit changeStepName(SourceNode left, SourceNode right){
        return new Edit(Type.CHANGE_STEP, left, right);
    }

    public static Edit changeValueName(SourceNode left, SourceNode right){
        return new Edit(Type.CHANGE_VALUE_NAME, left, right);
    }

    public static Edit changeValueType(SourceNode left, SourceNode right){
        return new Edit(Type.CHANGE_VALUE_TYPE, left, right);
    }

    public static Edit changeVariableDefinition(SourceNode left, SourceNode right) {
        return new Edit(Type.CHANGE_VARIABLE_DEFINITION, left, right);
    }

    public static Edit addDocumentation(SourceNode right) {
        return new Edit(Type.ADD_DOCUMENTATION, null, right);
    }

    public static Edit removeDocumentation(SourceNode left) {
        return new Edit(Type.REMOVE_DOCUMENTATION, left, null);
    }

    public static Edit changeDocumentation(SourceNode left, SourceNode right) {
        return new Edit(Type.CHANGE_DOCUMENTATION, left, right);
    }

    public static Edit invalid(SourceNode left, SourceNode right) {
        return new Edit(Type.INVALID, left, right);
    }
}
