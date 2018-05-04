package lu.uni.serval.utils.tree;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;

public class EditActionSerializer extends StdSerializer<EditAction> {
    public EditActionSerializer() {
        this(null);
    }

    protected EditActionSerializer(Class<EditAction> t) {
        super(t);
    }

    @Override
    public void serialize(EditAction editAction, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException, JsonGenerationException {
        jsonGenerator.writeStartObject();

        jsonGenerator.writeStringField("operation", editAction.operation.toString());

        String before = "";
        String after = "";
        switch (editAction.operation){
            case Replace:
                before = editAction.getNodeLabel1();
                after = editAction.getNodeLabel2();
                break;
            case Delete:
                before = editAction.getNodeLabel1();
                break;
            case Insert:
                after = editAction.getNodeLabel2();
                break;
        }

        jsonGenerator.writeStringField("before", before);
        jsonGenerator.writeStringField("after", after);

        jsonGenerator.writeEndObject();
    }
}
