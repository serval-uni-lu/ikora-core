package lu.uni.serval.analytics;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public class DifferenceResultsSerializer extends JsonSerializer<DifferenceResults> {
    @Override
    public void serialize(DifferenceResults results, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException, JsonProcessingException {
        jsonGenerator.writeStartArray();


        for(Difference difference: results.values()){
            if(difference.isEmpty()){
                continue;
            }

            jsonGenerator.writeObject(difference);
        }

        jsonGenerator.writeEndArray();
    }
}
