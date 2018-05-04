package lu.uni.serval.analytics;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import lu.uni.serval.utils.tree.EditAction;
import lu.uni.serval.utils.tree.EditOperation;
import org.apache.commons.lang3.tuple.Pair;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public class DifferenceResultSerializer extends StdSerializer<DifferenceResults>{
        public DifferenceResultSerializer() {
            this(null);
        }

        public DifferenceResultSerializer(Class<DifferenceResults> t) {
            super(t);
        }

    @Override
    public void serialize(DifferenceResults differenceResults, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException, JsonGenerationException {
        jsonGenerator.writeStartObject();

        jsonGenerator.writeStringField("generated", LocalDateTime.now().toString());

        for(Map.Entry<EditOperation, Integer> operations: differenceResults.counter.getOperations().entrySet()){
            jsonGenerator.writeStringField(operations.getKey().name().toLowerCase(), operations.getValue().toString());
        }

        jsonGenerator.writeArrayFieldStart("entries");
        for(DifferenceResults.Entry<Pair<LocalDateTime, LocalDateTime>, List<EditAction>> entry: differenceResults.entrySet()){
            jsonGenerator.writeStartObject();
            Pair<LocalDateTime, LocalDateTime> key = entry.getKey();

            for(Map.Entry<EditOperation, Integer> operations: differenceResults.counter.getOperations(key).entrySet()){
                jsonGenerator.writeStringField(operations.getKey().name().toLowerCase(), operations.getValue().toString());
            }

            jsonGenerator.writeStringField("from", key.getLeft() == null ? "unknown" : key.getLeft().toString());
            jsonGenerator.writeStringField("to", key.getRight() == null ? "unknown" : key.getRight().toString());

            jsonGenerator.writeArrayFieldStart("differences");

            for(EditAction difference: entry.getValue()){
                jsonGenerator.writeObject(difference);
            }

            jsonGenerator.writeEndArray();
            jsonGenerator.writeEndObject();
        }
        jsonGenerator.writeEndArray();
        jsonGenerator.writeEndObject();
    }
}
