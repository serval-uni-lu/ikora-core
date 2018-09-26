package lu.uni.serval.analytics;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import lu.uni.serval.robotframework.model.Project;
import lu.uni.serval.robotframework.model.TestCase;
import lu.uni.serval.robotframework.model.UserKeyword;

import java.io.IOException;
import java.util.Set;

public class ReportEvolutionSerializer extends JsonSerializer<EvolutionResults> {
    @Override
    public void serialize(EvolutionResults results, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException, JsonProcessingException {
        jsonGenerator.writeStartArray();

        for(Project project1: results.getProjects()){
            ProjectStatistics statistics = new ProjectStatistics(project1);

            jsonGenerator.writeStartObject();

            jsonGenerator.writeStringField("commit ID", project1.getCommitId());
            jsonGenerator.writeStringField("time", project1.getDateTime().toString());

            jsonGenerator.writeNumberField("number files", statistics.getNumberFiles());
            jsonGenerator.writeNumberField("number keywords", statistics.getNumberKeywords(UserKeyword.class));
            jsonGenerator.writeNumberField("number test cases", statistics.getNumberKeywords(TestCase.class));
            jsonGenerator.writeNumberField("lines of code", statistics.getLoc());

            Set<Project> compareTo = results.getComparedTo(project1);

            if(compareTo.size() > 1){
                throw new IOException("Expected only 1 project to compare to");
            }

            for(Project project2: compareTo){
                writeDifferences(results, jsonGenerator, project1, project2);
            }

            jsonGenerator.writeEndObject();
        }

        jsonGenerator.writeEndArray();
    }

    private void writeDifferences(EvolutionResults results, JsonGenerator jsonGenerator, Project project1, Project project2) throws IOException {
        DifferencesJson changes = new DifferencesJson();

        for(Difference difference: results.getDifferences(project1, project2)){
            changes.add(difference);
        }

        changes.writeJson(jsonGenerator, Project.class);
    }
}
