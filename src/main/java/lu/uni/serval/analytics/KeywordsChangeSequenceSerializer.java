package lu.uni.serval.analytics;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import lu.uni.serval.robotframework.model.KeywordDefinition;

import java.io.IOException;

public class KeywordsChangeSequenceSerializer extends JsonSerializer<EvolutionResults> {
    @Override
    public void serialize(EvolutionResults results, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException, JsonProcessingException {
        jsonGenerator.writeStartArray();

        for(TimeLine timeLine: results.getTimeLines()){
            if(!timeLine.isKeywordDefinition()){
                continue;
            }

            writeTimeLine(jsonGenerator, timeLine);
        }

        jsonGenerator.writeEndArray();
    }

    private void writeTimeLine(JsonGenerator jsonGenerator, TimeLine timeLine) throws IOException {
        jsonGenerator.writeStartObject();

        jsonGenerator.writeStringField("type", timeLine.getType());
        jsonGenerator.writeStringField("name", timeLine.getName());

        jsonGenerator.writeArrayFieldStart("changes");
        for (Difference difference: timeLine){
            writeDifference(jsonGenerator, difference);
        }
        jsonGenerator.writeEndArray();

        jsonGenerator.writeEndObject();
    }

    private void writeDifference(JsonGenerator jsonGenerator, Difference difference) throws IOException {
        jsonGenerator.writeStartObject();

        KeywordDefinition keyword = (KeywordDefinition)difference.getValue();

        jsonGenerator.writeNumberField("time", keyword.getEpoch());
        jsonGenerator.writeArrayFieldStart("actions");

        for (Action action: difference.getActions()){
            jsonGenerator.writeString(action.getType().name());
        }

        jsonGenerator.writeEndArray();

        jsonGenerator.writeEndObject();
    }
}
