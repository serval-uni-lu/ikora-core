package lu.uni.serval.analytics;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import lu.uni.serval.utils.ReportKeywordData;
import org.apache.commons.lang3.tuple.Pair;

import java.io.IOException;
import java.time.LocalDateTime;

public class StatusResultSerializer extends StdSerializer<StatusResults> {
    public StatusResultSerializer(){
        this(null);
    }
    public StatusResultSerializer(Class<StatusResults> t){
        super(t);
    }

    @Override
    public void serialize(StatusResults results, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException, JsonProcessingException {
        jsonGenerator.writeStartObject();

        writeTests(results, jsonGenerator);

        jsonGenerator.writeArrayFieldStart("total");
        writeTotal(results, jsonGenerator, ReportKeywordData.Status.PASS);
        writeTotal(results, jsonGenerator, ReportKeywordData.Status.FAILED);
        jsonGenerator.writeEndArray();

        jsonGenerator.writeEndObject();
    }

    private void writeTotal(StatusResults results, JsonGenerator jsonGenerator, ReportKeywordData.Status status) throws IOException {
        jsonGenerator.writeStartObject();

        jsonGenerator.writeStringField("status", status.name());

        jsonGenerator.writeArrayFieldStart("sequence");
        for(Pair<LocalDateTime, Integer> pair: results.getTotal(status)){
            jsonGenerator.writeStartObject();

            jsonGenerator.writeStringField("date", pair.getLeft().toString());
            jsonGenerator.writeNumberField("status", pair.getRight());

            jsonGenerator.writeEndObject();
        }
        jsonGenerator.writeEndArray();

        jsonGenerator.writeEndObject();
    }

    private void writeTests(StatusResults results, JsonGenerator jsonGenerator) throws IOException {
        jsonGenerator.writeArrayFieldStart("tests");
        for(StatusResults.KeywordInfo info: results.getKeywordsInfo()){
            jsonGenerator.writeStartObject();

            jsonGenerator.writeStringField("name", info.getName());
            jsonGenerator.writeStringField("file", info.getFile());
            jsonGenerator.writeNumberField("size", info.getSize());
            jsonGenerator.writeNumberField("number of actions", info.getNumberLeaves());

            jsonGenerator.writeArrayFieldStart("sequence");
            for(Pair<LocalDateTime, ReportKeywordData.Status> pair: results.getKeyword(info)){
                jsonGenerator.writeStartObject();

                jsonGenerator.writeStringField("date", pair.getLeft().toString());
                jsonGenerator.writeStringField("status", pair.getRight().name());

                jsonGenerator.writeEndObject();
            }
            jsonGenerator.writeEndArray();

            jsonGenerator.writeEndObject();
        }
        jsonGenerator.writeEndArray();
    }
}
