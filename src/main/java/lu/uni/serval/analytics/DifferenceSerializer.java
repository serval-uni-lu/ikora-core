package lu.uni.serval.analytics;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import lu.uni.serval.robotframework.model.*;
import lu.uni.serval.utils.Differentiable;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.List;

public class DifferenceSerializer extends JsonSerializer<Difference> {
    final static Logger logger = Logger.getLogger(DifferenceSerializer.class);

    @Override
    public void serialize(Difference difference, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException, JsonProcessingException {
        writeActions(difference, jsonGenerator);
    }

    private void writeActions(Difference difference, JsonGenerator jsonGenerator) throws IOException {

        for(Action action: difference.getActions()){
            jsonGenerator.writeStartObject();

            switch (action.getType()){
                case ADD_USER_KEYWORD:
                {
                    jsonGenerator.writeStringField("type", "add user keyword");
                    writeKeywordInfo(jsonGenerator, difference.getRight(), "user keyword", true);
                }
                break;

                case REMOVE_USER_KEYWORD:
                {
                    jsonGenerator.writeStringField("type", "remove user keyword");
                    writeKeywordInfo(jsonGenerator, difference.getLeft(), "user keyword", true);
                }
                break;

                case ADD_STEP:
                {
                    int position = action.getRight();
                    jsonGenerator.writeStringField("type", "add step");
                    jsonGenerator.writeStringField("step", getStepName(difference.getRight(), position));
                    jsonGenerator.writeNumberField("position", position);

                    writeKeywordInfo(jsonGenerator, difference.getLeft(), "keyword", false);
                }
                break;

                case REMOVE_STEP:
                {
                    int position = action.getLeft();
                    jsonGenerator.writeStringField("type", "remove step");
                    jsonGenerator.writeStringField("step", getStepName(difference.getLeft(), position));
                    jsonGenerator.writeNumberField("position", position);

                    writeKeywordInfo(jsonGenerator, difference.getLeft(), "keyword", false);
                }
                break;

                case CHANGE_STEP:
                {
                    jsonGenerator.writeStringField("type", "change step");

                    int positionBefore = action.getLeft();
                    jsonGenerator.writeStringField("before", getStepName(difference.getLeft(), positionBefore));

                    int positionAfter = action.getRight();
                    jsonGenerator.writeStringField("after", getStepName(difference.getRight(), positionAfter));

                    writeKeywordInfo(jsonGenerator, difference.getLeft(), "keyword", false);
                }
                break;

                case CHANGE_STEP_ARGUMENTS:
                {
                    jsonGenerator.writeStringField("type", "change step arguments");

                    int positionBefore = action.getLeft();
                    KeywordCall callBefore = (KeywordCall) ((KeywordDefinition)difference.getLeft()).getStep(positionBefore);
                    jsonGenerator.writeStringField("before", listToString(callBefore.getParameters()));

                    int positionAfter = action.getRight();
                    KeywordCall callAfter = (KeywordCall) ((KeywordDefinition)difference.getRight()).getStep(positionAfter);
                    jsonGenerator.writeStringField("after", listToString(callAfter.getParameters()));

                    writeKeywordInfo(jsonGenerator, difference.getLeft(), "keyword", false);
                }
                break;

                case CHANGE_STEP_EXPRESSION:
                {
                    jsonGenerator.writeStringField("type", "change step expression");

                    int positionBefore = action.getLeft();
                    Assignment assignmentBefore = (Assignment) ((KeywordDefinition)difference.getLeft()).getStep(positionBefore);
                    jsonGenerator.writeStringField("before", assignmentBefore.getExpression().toString());

                    int positionAfter = action.getLeft();
                    Assignment assignmentAfter = (Assignment) ((KeywordDefinition)difference.getRight()).getStep(positionAfter);
                    jsonGenerator.writeStringField("after", assignmentAfter.getExpression().toString());

                    writeKeywordInfo(jsonGenerator, difference.getLeft(), "keyword", false);
                }
                break;

                case CHANGE_STEP_TYPE:
                {
                    jsonGenerator.writeStringField("type", "change step type");

                    int positionBefore = action.getLeft();
                    Keyword stepBefore = ((KeywordDefinition)difference.getLeft()).getStep(positionBefore);
                    jsonGenerator.writeStringField("before", getStepType(stepBefore));

                    int positionAfter = action.getLeft();
                    Keyword stepAfter = ((KeywordDefinition)difference.getRight()).getStep(positionAfter);
                    jsonGenerator.writeStringField("after", getStepType(stepAfter));

                    writeKeywordInfo(jsonGenerator, difference.getLeft(), "keyword", false);
                }
                break;

                case CHANGE_NAME:
                {
                    jsonGenerator.writeStringField("type", "change keyword name");

                    jsonGenerator.writeStringField("before", ((KeywordDefinition)difference.getLeft()).getName().toString());
                    jsonGenerator.writeStringField("after", ((KeywordDefinition)difference.getRight()).getName().toString());

                    jsonGenerator.writeStringField("file", ((KeywordDefinition)difference.getRight()).getFile());
                }
                break;

                default:
                    logger.warn("Unhandled action type " + action.getType().name());
                    break;
            }

            jsonGenerator.writeEndObject();
        }
    }

    private void writeKeywordInfo(JsonGenerator jsonGenerator, Differentiable differentiable, String name, boolean showSteps) throws IOException {
        if(!(differentiable instanceof KeywordDefinition)){
            return;
        }

        KeywordDefinition keyword =(KeywordDefinition)differentiable;

        jsonGenerator.writeObjectFieldStart(name);

        jsonGenerator.writeStringField("file", keyword.getFile());
        jsonGenerator.writeStringField("name", keyword.getName().toString());

        if(showSteps){
            jsonGenerator.writeArrayFieldStart("steps");

            for(Step step: keyword){
                jsonGenerator.writeString(step.getName().toString());
            }

            jsonGenerator.writeEndArray();
        }

        jsonGenerator.writeEndObject();
    }

    private String listToString(List<Argument> arguments){
        StringBuilder builder = new StringBuilder();

        for(Argument argument: arguments){
            if(builder.length() != 0){
                builder.append("\t");
            }

            builder.append(argument.toString());
        }

        return builder.toString();
    }

    private String getStepType(Keyword step){
        String type = "Unknown Type";

        if(step instanceof KeywordCall){
            type = "Keyword Call";
        }
        else if (step instanceof Assignment) {
            type = "Assignment";
        }
        else if (step instanceof ForLoop) {
            type = "For Loop";
        }

        return type;
    }

    private String getStepName(Differentiable differentiable, int position){
        if(!(differentiable instanceof KeywordDefinition)){
            logger.warn("Expecting a KeywordDefinition got " + differentiable.getClass().getSimpleName() + " instead");
            return "INVALID STEP";
        }

        return ((KeywordDefinition)differentiable).getStep(position).getName().toString();
    }
}
