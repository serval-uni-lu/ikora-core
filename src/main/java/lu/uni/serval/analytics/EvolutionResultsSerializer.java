package lu.uni.serval.analytics;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import lu.uni.serval.robotframework.model.Project;

import java.io.IOException;
import java.util.Set;

public class EvolutionResultsSerializer extends JsonSerializer<EvolutionResults> {
    @Override
    public void serialize(EvolutionResults results, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException, JsonProcessingException {
        jsonGenerator.writeStartArray();

        for(Project project1: results.getProjects()){
            jsonGenerator.writeStartObject();

            jsonGenerator.writeStringField("commit ID", project1.getCommitId());
            jsonGenerator.writeStringField("time", project1.getDateTime().toString());

            Set<Project> compareTo = results.getComparedTo(project1);
/*
            if(compareTo.size() > 1){
                throw new IOException("Expected only 1 project to compare to");
            }
*/
            for(Project project2: compareTo){
                writeDifferences(results, jsonGenerator, project1, project2);
            }

            jsonGenerator.writeEndObject();
        }

        jsonGenerator.writeEndArray();
    }

    private void writeDifferences(EvolutionResults results, JsonGenerator jsonGenerator, Project project1, Project project2) throws IOException {
        jsonGenerator.writeObjectFieldStart("compare");

        jsonGenerator.writeStringField("commit ID", project2.getCommitId());
        jsonGenerator.writeStringField("time", project2.getDateTime().toString());

        jsonGenerator.writeArrayFieldStart("differences");
        for(Difference difference: results.getDifferences(project1, project2)){
            if(difference.isEmpty()){
                continue;
            }

            jsonGenerator.writeObject(difference);
        }
        jsonGenerator.writeEndArray();

        jsonGenerator.writeEndObject();
    }
}
