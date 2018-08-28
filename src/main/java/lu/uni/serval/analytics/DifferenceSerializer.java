package lu.uni.serval.analytics;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import lu.uni.serval.robotframework.model.Argument;
import lu.uni.serval.robotframework.model.KeywordCall;
import lu.uni.serval.robotframework.model.KeywordDefinition;
import lu.uni.serval.robotframework.model.Step;

import java.io.IOException;
import java.util.List;

public class DifferenceSerializer extends JsonSerializer<Difference> {
    @Override
    public void serialize(Difference difference, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException, JsonProcessingException {
        jsonGenerator.writeStartObject();

        writeActions(difference, jsonGenerator);

        jsonGenerator.writeEndObject();
    }

    private void writeActions(Difference difference, JsonGenerator jsonGenerator) throws IOException {
        jsonGenerator.writeArrayFieldStart("actions");

        KeywordDefinition before = difference.getLeft();
        KeywordDefinition after = difference.getRight();

        for(Action action: difference.getActions()){
            jsonGenerator.writeStartObject();

            switch (action.getType()){
                case ADD_USER_KEYWORD:
                {
                    jsonGenerator.writeStringField("type", "add user keyword");
                    writeKeywordInfo(jsonGenerator, after, "user keyword", true);
                }
                break;

                case REMOVE_USER_KEYWORD:
                {
                    jsonGenerator.writeStringField("type", "remove user keyword");
                    writeKeywordInfo(jsonGenerator, before, "user keyword", true);
                }
                break;

                case ADD_STEP:
                {
                    int position = action.getRight();
                    jsonGenerator.writeStringField("type", "add step");
                    jsonGenerator.writeStringField("step", after.getStep(position).getName().toString());
                    jsonGenerator.writeNumberField("position", position);
                }
                break;

                case REMOVE_STEP:
                {
                    int position = action.getLeft();
                    jsonGenerator.writeStringField("type", "remove step");
                    jsonGenerator.writeStringField("step", before.getStep(position).getName().toString());
                    jsonGenerator.writeNumberField("position", position);
                }
                break;

                case CHANGE_STEP_NAME:
                {
                    jsonGenerator.writeStringField("type", "change name");

                    int positionBefore = action.getLeft();
                    jsonGenerator.writeStringField("before", before.getStep(positionBefore).getName().toString());

                    int positionAfter = action.getRight();
                    jsonGenerator.writeStringField("after", after.getStep(positionAfter).getName().toString());
                }
                break;

                case CHANGE_STEP_ARGUMENTS:
                {
                    jsonGenerator.writeStringField("type", "change arguments");

                    int positionBefore = action.getLeft();
                    KeywordCall callBefore = (KeywordCall) before.getStep(positionBefore);
                    jsonGenerator.writeStringField("before", listToString(callBefore.getParameters()));

                    int positionAfter = action.getRight();
                    KeywordCall callAfter = (KeywordCall) after.getStep(positionAfter);
                    jsonGenerator.writeStringField("after", listToString(callAfter.getParameters()));
                }
                break;

                default:
                    System.out.println("Unhandled");
                    break;
            }

            if(difference.getLeft() != null && difference.getRight() != null){
                writeKeywordInfo(jsonGenerator, before, "keyword name before", false);
                writeKeywordInfo(jsonGenerator, after, "keyword name after", false);
            }

            jsonGenerator.writeEndObject();
        }

        jsonGenerator.writeEndArray();
    }

    private void writeKeywordInfo(JsonGenerator jsonGenerator, KeywordDefinition keyword, String name, boolean showSteps) throws IOException {
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
}
