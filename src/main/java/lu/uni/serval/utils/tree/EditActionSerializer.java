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

        if(editAction.operation != EditOperation.Insert){
            writeNodeObject(jsonGenerator, "before", editAction.getNode1());
        }

        if(editAction.operation != EditOperation.Delete){
            writeNodeObject(jsonGenerator, "after", editAction.getNode2());
        }

        jsonGenerator.writeEndObject();
    }

    private void writeNodeObject(JsonGenerator jsonGenerator, String name, LabelTreeNode node) throws IOException {
        jsonGenerator.writeObjectFieldStart(name);

        jsonGenerator.writeStringField("label", node == null ? "" :node.getLabel());
        jsonGenerator.writeStringField("parent", node == null || node.getParent() == null ? "null" : node.getParent().getLabel());
        jsonGenerator.writeStringField("size", node == null ? "0" : String.valueOf(node.getNodeCount()));

        jsonGenerator.writeEndObject();
    }
}
