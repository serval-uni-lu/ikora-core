package lu.uni.serval.analytics;

import com.fasterxml.jackson.core.JsonGenerator;
import lu.uni.serval.robotframework.model.Keyword;
import lu.uni.serval.utils.Differentiable;
import org.openqa.selenium.InvalidArgumentException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class DifferencesJson {
    private enum Type{
        ADD_USER_KEYWORD,
        REMOVE_USER_KEYWORD,
        CHANGE_NAME,

        ADD_TEST_CASE,
        REMOVE_TEST_CASE,

        CHANGE_SYNC_STEP,
        CHANGE_USER_STEP,
        CHANGE_ASSERT_STEP,
        CHANGE_LOG_STEP,
        CHANGE_CONTROLFLOW_STEP,
        CHANGE_ACTION_STEP,
        CHANGE_ERROR_STEP,
        CHANGE_GET_STEP,
        CHANGE_UNKNOWN_STEP,

        CHANGE_STEP_TYPE,
        CHANGE_STEP_RETURN_VALUES,

        CHANGE_FOR_LOOP_CONDITION,
        CHANGE_FOR_LOOP_BODY,

        ADD_VARIABLE,
        REMOVE_VARIABLE,
        CHANGE_VARIABLE_DEFINITION,

        INVALID
    }

    Map<Type, Integer> actions;

    public DifferencesJson() {
        this.actions = new HashMap<>();

        for(Type type: Type.values()){
            actions.put(type, 0);
        }
    }

    public void add(Difference difference) throws InvalidArgumentException {
        for(Action action: difference.getActions()){
            switch (action.getType()){
                case ADD_USER_KEYWORD:
                    actions.put(Type.ADD_USER_KEYWORD, actions.get(Type.ADD_USER_KEYWORD) + 1);
                    break;
                case REMOVE_USER_KEYWORD:
                    actions.put(Type.REMOVE_USER_KEYWORD, actions.get(Type.REMOVE_USER_KEYWORD) + 1);
                    break;
                case CHANGE_NAME:
                    actions.put(Type.CHANGE_NAME, actions.get(Type.CHANGE_NAME) + 1);
                    break;
                case ADD_TEST_CASE:
                    actions.put(Type.ADD_TEST_CASE, actions.get(Type.ADD_TEST_CASE) + 1);
                    break;
                case REMOVE_TEST_CASE:
                    actions.put(Type.REMOVE_TEST_CASE, actions.get(Type.REMOVE_TEST_CASE) + 1);
                    break;
                case CHANGE_STEP_TYPE:
                    actions.put(Type.CHANGE_STEP_TYPE, actions.get(Type.CHANGE_STEP_TYPE) + 1);
                    break;
                case CHANGE_STEP_RETURN_VALUES:
                    actions.put(Type.CHANGE_STEP_RETURN_VALUES, actions.get(Type.CHANGE_STEP_RETURN_VALUES) + 1);
                    break;
                case CHANGE_FOR_LOOP_CONDITION:
                    actions.put(Type.CHANGE_FOR_LOOP_CONDITION, actions.get(Type.CHANGE_FOR_LOOP_CONDITION) + 1);
                    break;
                case CHANGE_FOR_LOOP_BODY:
                    actions.put(Type.CHANGE_FOR_LOOP_BODY, actions.get(Type.CHANGE_FOR_LOOP_BODY) + 1);
                    break;
                case ADD_VARIABLE:
                    actions.put(Type.ADD_VARIABLE, actions.get(Type.ADD_VARIABLE) + 1);
                    break;
                case REMOVE_VARIABLE:
                    actions.put(Type.REMOVE_VARIABLE, actions.get(Type.REMOVE_VARIABLE) + 1);
                    break;
                case CHANGE_VARIABLE_DEFINITION:
                    actions.put(Type.CHANGE_VARIABLE_DEFINITION, actions.get(Type.CHANGE_VARIABLE_DEFINITION) + 1);
                    break;
                case INVALID:
                    actions.put(Type.INVALID, actions.get(Type.INVALID) + 1);
                    break;

                case ADD_STEP:
                case REMOVE_STEP:
                case CHANGE_STEP_EXPRESSION:
                case CHANGE_STEP:
                case CHANGE_STEP_ARGUMENTS:
                    changeStep(action);
                    break;
            }
        }
    }

    private void changeStep(Action action) throws InvalidArgumentException {
        Differentiable differentiable = action.getValue();

        if(!(differentiable instanceof Keyword)){
            throw new InvalidArgumentException("Expected a Keyword got " + differentiable.getClass() + " instead!");
        }

        Keyword keyword = (Keyword)differentiable;

        switch (keyword.getType()){

            case User:
                actions.put(Type.CHANGE_USER_STEP, actions.get(Type.CHANGE_USER_STEP) + 1);
                break;
            case ControlFlow:
                actions.put(Type.CHANGE_CONTROLFLOW_STEP, actions.get(Type.CHANGE_CONTROLFLOW_STEP) + 1);
                break;
            case Assertion:
                actions.put(Type.CHANGE_ASSERT_STEP, actions.get(Type.CHANGE_ASSERT_STEP) + 1);
                break;
            case Action:
                actions.put(Type.CHANGE_ACTION_STEP, actions.get(Type.CHANGE_ACTION_STEP) + 1);
                break;
            case Log:
                actions.put(Type.CHANGE_LOG_STEP, actions.get(Type.CHANGE_LOG_STEP) + 1);
                break;
            case Error:
                actions.put(Type.CHANGE_ERROR_STEP, actions.get(Type.CHANGE_ERROR_STEP) + 1);
                break;
            case Synchronisation:
                actions.put(Type.CHANGE_SYNC_STEP, actions.get(Type.CHANGE_SYNC_STEP) + 1);
                break;
            case Get:
                actions.put(Type.CHANGE_GET_STEP, actions.get(Type.CHANGE_GET_STEP) + 1);
                break;
            case Unknown:
                actions.put(Type.CHANGE_UNKNOWN_STEP, actions.get(Type.CHANGE_UNKNOWN_STEP) + 1);
                break;
        }
    }

    public void writeJson(JsonGenerator jsonGenerator) throws IOException {
        jsonGenerator.writeObjectFieldStart("changes");

        for(Type type: actions.keySet()){
            jsonGenerator.writeNumberField(cleanName(type.name()), actions.get(type));
        }

        jsonGenerator.writeEndObject();
    }

    private String cleanName(String raw){
        String clean = raw.replace('_', ' ');
        return clean.toLowerCase();
    }
}
