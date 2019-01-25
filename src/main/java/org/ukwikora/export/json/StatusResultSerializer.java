package org.ukwikora.export.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.ukwikora.analytics.StatusResults;
import org.ukwikora.report.KeywordStatus;
import org.ukwikora.report.Report;
import org.apache.commons.lang3.tuple.Pair;

import java.io.IOException;

public class StatusResultSerializer extends StdSerializer<StatusResults> {
    public StatusResultSerializer(){
        this(null);
    }
    public StatusResultSerializer(Class<StatusResults> t){
        super(t);
    }

    @Override
    public void serialize(StatusResults results, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();

        writeTests(results, jsonGenerator);

        jsonGenerator.writeArrayFieldStart("total");
        writeTotal(results, jsonGenerator, KeywordStatus.Status.PASS);
        writeTotal(results, jsonGenerator, KeywordStatus.Status.FAIL);
        jsonGenerator.writeEndArray();

        jsonGenerator.writeEndObject();
    }

    private void writeTotal(StatusResults results, JsonGenerator jsonGenerator, KeywordStatus.Status status) throws IOException {
        jsonGenerator.writeStartObject();

        jsonGenerator.writeStringField("status", status.name());

        jsonGenerator.writeArrayFieldStart("sequence");
        for(Pair<Report, Integer> pair: results.getTotal(status)){
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

            jsonGenerator.writeArrayFieldStart("sequence");
            for(Pair<Report, KeywordStatus> pair: results.getKeyword(info)){
                /*
                if(results.isServiceDown(pair.getValue())){
                    continue;
                }
                */

                jsonGenerator.writeStartObject();

                //jsonGenerator.writeStringField("date", pair.getValue().getExecutionDate());
                jsonGenerator.writeStringField("status", pair.getValue().getName());
                //jsonGenerator.writeNumberField("size", pair.getValue().getNodeCount());
                //jsonGenerator.writeNumberField("number of actions", pair.getValue().getLeavesSize());

                jsonGenerator.writeEndObject();
            }
            jsonGenerator.writeEndArray();

            jsonGenerator.writeEndObject();
        }
        jsonGenerator.writeEndArray();
    }
}
