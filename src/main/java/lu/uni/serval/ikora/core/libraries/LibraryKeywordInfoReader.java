package lu.uni.serval.ikora.core.libraries;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import lu.uni.serval.ikora.core.types.*;
import lu.uni.serval.ikora.core.model.Keyword;

import java.io.IOException;


public class LibraryKeywordInfoReader extends StdDeserializer<LibraryKeywordInfo> {
    public LibraryKeywordInfoReader() {
        this(null);
    }

    public LibraryKeywordInfoReader(Class<?> vc) {
        super(vc);
    }

    @Override
    public LibraryKeywordInfo deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        final JsonNode node = jsonParser.getCodec().readTree(jsonParser);

        final Keyword.Type type = Keyword.Type.valueOf(node.get("type").textValue());
        final String name = node.get("name").textValue();

        final BaseTypeList argumentTypes = new BaseTypeList();
        for(final JsonNode argumentNode: node.get("arguments")){
            argumentTypes.add(createArgumentType(jsonParser, argumentNode));
        }

        return new LibraryKeywordInfo(type, name, argumentTypes);
    }

    private BaseType createArgumentType(JsonParser jsonParser, JsonNode node) throws JsonParseException {
        String type = node.get("type").textValue();
        String name = node.get("name").textValue();
        String defaultValue = node.has("default") ? node.get("default").textValue() : null;

        BaseType argumentType;

        switch (type){
            case "Boolean": argumentType = new BooleanType(name, defaultValue); break;
            case "Condition": argumentType = new ConditionType(name, defaultValue); break;
            case "Dictionary": argumentType = new DictionaryType(name, defaultValue); break;
            case "Keyword": argumentType = new KeywordType(name, defaultValue); break;
            case "List": argumentType = new ListType(name, defaultValue); break;
            case "Locator": argumentType = new LocatorType(name, defaultValue); break;
            case "LogLevel": argumentType = new LogLevelType(name, defaultValue); break;
            case "Number": argumentType = new NumberType(name, defaultValue); break;
            case "Object": argumentType = new ObjectType(name, defaultValue); break;
            case "Path": argumentType = new PathType(name, defaultValue); break;
            case "String": argumentType = new StringType(name, defaultValue); break;
            case "Timeout": argumentType = new TimeoutType(name, defaultValue); break;

            default: throw new JsonParseException(jsonParser, String.format("Invalid argument type: %s", type));
        }

        return argumentType;
    }
}
