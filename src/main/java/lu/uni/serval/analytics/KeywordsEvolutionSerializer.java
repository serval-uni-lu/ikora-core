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

        jsonGenerator.writeStringField("type", keyword.getClass().getSimpleName());
        jsonGenerator.writeStringField("file", keyword.getFile());
        jsonGenerator.writeStringField("name", keyword.getName().toString() );
        jsonGenerator.writeStringField("commit id", project.getCommitId());
        jsonGenerator.writeNumberField("number steps", keyword.getSteps().size());
        jsonGenerator.writeNumberField("sequence size", keyword.getMaxSequenceSize());
        jsonGenerator.writeNumberField("number branches", keyword.getBranchIndex());
        jsonGenerator.writeNumberField("size", keyword.getSize());
        jsonGenerator.writeNumberField("depth", keyword.getDepth());
        jsonGenerator.writeNumberField("connectivity", keyword.getConnectivity(-1));
        jsonGenerator.writeNumberField("number changes", difference.getActions().size());

        jsonGenerator.writeEndObject();
    }
}
