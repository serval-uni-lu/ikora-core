package lu.uni.serval.analytics;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import lu.uni.serval.robotframework.model.Project;

import java.io.IOException;

public class EvolutionResultsSerializer extends JsonSerializer<EvolutionResults> {
    @Override
    public void serialize(EvolutionResults results, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException, JsonProcessingException {
        jsonGenerator.writeStartArray();

        for(Project project1: results.getProjects()){

            for(Project project2: results.getComparedTo(project1)){

                for(Difference difference: results.getDifferences(project1, project2)){
                    if(difference.isEmpty()){
                        continue;
                    }

                    jsonGenerator.writeObject(difference);
                }
            }
        }

        jsonGenerator.writeEndArray();
    }
}
