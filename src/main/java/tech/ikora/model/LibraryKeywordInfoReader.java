package tech.ikora.model;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class LibraryKeywordInfoReader extends StdDeserializer<LibraryKeywordInfo> {
    public LibraryKeywordInfoReader() {
        this(null);
    }

    public LibraryKeywordInfoReader(Class<?> vc) {
        super(vc);
    }

    @Override
    public LibraryKeywordInfo deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        final JsonNode node = jsonParser.getCodec().readTree(jsonParser);

        final Keyword.Type type = Keyword.Type.valueOf(node.get("type").textValue());
        final String name = node.get("name").textValue();
        final List<Argument.Type> argumentTypes = new ArrayList<>();

        for(final JsonNode argumentTypeNode: node.get("argument types")){
            argumentTypes.add(Argument.Type.valueOf(argumentTypeNode.textValue()));
        }

        return new LibraryKeywordInfo(type, name, argumentTypes.toArray(new Argument.Type[argumentTypes.size()]));
    }
}
