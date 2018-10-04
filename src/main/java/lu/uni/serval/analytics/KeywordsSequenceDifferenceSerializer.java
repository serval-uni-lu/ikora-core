package lu.uni.serval.analytics;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import lu.uni.serval.robotframework.model.Element;
import lu.uni.serval.utils.Differentiable;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class KeywordsSequenceDifferenceSerializer extends JsonSerializer<EvolutionResults> {

    @Override
    public void serialize(EvolutionResults results, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException, JsonProcessingException {
        jsonGenerator.writeStartObject();

        writeCoevolution(jsonGenerator, results.getTimeLinesMatches());
        writeNotChanged(jsonGenerator, results.getNotChanged());

        jsonGenerator.writeEndObject();
    }

    private void writeNotChanged(JsonGenerator jsonGenerator, List<TimeLine> notChanged) throws IOException {
        jsonGenerator.writeArrayFieldStart("not changed");

        for(TimeLine timeLine: notChanged){
            if(!timeLine.isKeywordDefinition()){
                continue;
            }

            jsonGenerator.writeStartObject();
            writeTimeLineInfo(jsonGenerator, timeLine);
            jsonGenerator.writeEndObject();
        }

        jsonGenerator.writeEndArray();
    }

    private void writeCoevolution(JsonGenerator jsonGenerator, DifferentiableMatcher timeLinesMatches) throws IOException {
        jsonGenerator.writeArrayFieldStart("coevolution");

        Map<Differentiable, Set<Differentiable>> matched = timeLinesMatches.getMatched();

        for(Differentiable differentiable1: matched.keySet()){
            TimeLine timeLine = (TimeLine)differentiable1;

            if(!timeLine.isKeywordDefinition()){
                continue;
            }

            jsonGenerator.writeStartObject();
            writeTimeLineInfo(jsonGenerator, timeLine);

            jsonGenerator.writeArrayFieldStart("similar");
            for(Differentiable differentiable2: matched.get(timeLine)){
                TimeLine similar = (TimeLine)differentiable2;

                jsonGenerator.writeStartObject();
                writeTimeLineInfo(jsonGenerator, similar);
                jsonGenerator.writeNumberField("similarity", 1 - timeLine.distance(similar));
                jsonGenerator.writeEndObject();
            }
            jsonGenerator.writeEndArray();

            jsonGenerator.writeEndObject();
        }

        jsonGenerator.writeEndArray();
    }

    private void writeTimeLineInfo(JsonGenerator jsonGenerator, TimeLine timeLine) throws IOException {
        jsonGenerator.writeStringField("type", timeLine.getType());
        //jsonGenerator.writeStringField("file", getFileName(timeLine));
        jsonGenerator.writeStringField("name", timeLine.getName());
        //writeActions(jsonGenerator, timeLine);
    }

    private void writeActions(JsonGenerator jsonGenerator, TimeLine timeLine) throws IOException {
        jsonGenerator.writeArrayFieldStart("actions");

        for(Action action: timeLine.getActions()){
            jsonGenerator.writeStartObject();
            jsonGenerator.writeStringField("type", action.getType().name());
            jsonGenerator.writeStringField("value", action.getValue().getName());
            jsonGenerator.writeEndObject();
        }

        jsonGenerator.writeEndArray();
    }

    private String getFileName(TimeLine timeLine){
        Differentiable last = timeLine.getLastValid();

        if(last == null){
            return "";
        }

        if(!Element.class.isAssignableFrom(last.getClass())){
            return "";
        }

        return ((Element)last).getFile().getName();
    }
}
