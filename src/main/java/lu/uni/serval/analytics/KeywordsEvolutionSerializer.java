package lu.uni.serval.analytics;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import lu.uni.serval.robotframework.model.*;

import java.io.IOException;

public class KeywordsEvolutionSerializer extends JsonSerializer<EvolutionResults> {
    @Override
    public void serialize(EvolutionResults results, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException, JsonProcessingException {
        jsonGenerator.writeStartArray();

        for(Project project: results.getProjects()){
            for(TestCase keyword: project.getElements(TestCase.class)){
                Difference difference = results.getDifference(keyword);
                writeKeyword(jsonGenerator, keyword, difference, project);
            }
        }

        for(Project project: results.getProjects()){
            for(UserKeyword keyword: project.getElements(UserKeyword.class)){
                Difference difference = results.getDifference(keyword);
                writeKeyword(jsonGenerator, keyword, difference, project);
            }
        }

        jsonGenerator.writeEndArray();
    }

    private void writeKeyword(JsonGenerator jsonGenerator, KeywordDefinition keyword, Difference difference, Project project) throws IOException {
        jsonGenerator.writeStartObject();

        jsonGenerator.writeNumberField("number steps", keyword.getSteps().size());
        jsonGenerator.writeNumberField("sequence size", keyword.getMaxSequenceSize());
        jsonGenerator.writeNumberField("number branches", keyword.getBranchIndex());
        jsonGenerator.writeNumberField("size", keyword.getSize());
        jsonGenerator.writeNumberField("depth", keyword.getDepth());
        jsonGenerator.writeNumberField("connectivity", keyword.getConnectivity(-1));

        writeChanges(jsonGenerator, difference);

        jsonGenerator.writeEndObject();
    }

    private void writeChanges(JsonGenerator jsonGenerator, Difference difference) throws IOException {
        int totalChanges = 0;
        int changeStep = 0;
        int changeStepArguments = 0;
        int changeName = 0;

        for(Action action: difference.getActions()){
            switch (action.getType()){
                case CHANGE_NAME:
                    ++changeName;
                    break;

                case CHANGE_FOR_LOOP_CONDITION:
                case CHANGE_FOR_LOOP_BODY:
                case CHANGE_STEP_EXPRESSION:
                case CHANGE_STEP_RETURN_VALUES:
                case ADD_STEP:
                case REMOVE_STEP:
                case CHANGE_STEP_TYPE:
                case CHANGE_STEP:
                    ++changeStep;
                    break;

                case CHANGE_STEP_ARGUMENTS:
                    ++changeStepArguments;
                    break;

                case INVALID:
                    break;
            }

            ++totalChanges;
        }

        jsonGenerator.writeNumberField("changes total", totalChanges);
        jsonGenerator.writeNumberField("changes step", changeStep);
        jsonGenerator.writeNumberField("changes step arguments", changeStepArguments);
        jsonGenerator.writeNumberField("changes name", changeName);
    }
}
