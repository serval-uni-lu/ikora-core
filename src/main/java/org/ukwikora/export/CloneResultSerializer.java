package org.ukwikora.export;


import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.ukwikora.analytics.CloneResults;

import java.io.IOException;

public class CloneResultSerializer extends StdSerializer<CloneResults> {
    public CloneResultSerializer() {
        this(null);
    }

    public CloneResultSerializer(Class<CloneResults> t) {
        super(t);
    }

    @Override
    public void serialize(CloneResults cloneResults, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeArrayFieldStart("clones");

        jsonGenerator.writeEndObject();
    }
}
