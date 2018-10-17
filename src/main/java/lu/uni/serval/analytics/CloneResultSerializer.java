package lu.uni.serval.analytics;


import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import lu.uni.serval.robotframework.model.KeywordDefinition;
import lu.uni.serval.utils.CompareCache;
import lu.uni.serval.utils.UnorderedPair;
import lu.uni.serval.utils.tree.LabelTreeNode;

import java.io.IOException;
import java.util.Map;

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
