/*
 *
 *     Copyright © 2019 - 2022 University of Luxembourg
 *
 *     Licensed under the Apache License, Version 2.0 (the "License")
 *     you may not use this file except in compliance with the License.
 *     You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *     Unless required by applicable law or agreed to in writing, software
 *     distributed under the License is distributed on an "AS IS" BASIS,
 *     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *     See the License for the specific language governing permissions and
 *     limitations under the License.
 */
package lu.uni.serval.ikora.core.libraries;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import lu.uni.serval.ikora.core.runner.exception.InvalidTypeException;
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
        try {
            final JsonNode node = jsonParser.getCodec().readTree(jsonParser);

            final Keyword.Type type = Keyword.Type.valueOf(node.get("type").textValue());
            final String name = node.get("name").textValue();

            final BaseTypeList argumentTypes = new BaseTypeList();
            for(final JsonNode argumentNode: node.get("arguments")){
                    argumentTypes.add(createArgumentType(jsonParser, argumentNode));
            }

            return new LibraryKeywordInfo(type, name, argumentTypes);
        } catch (InvalidTypeException e) {
            throw new IOException(String.format("Invalid type detected: %s", e.getMessage()));
        }
    }

    private BaseType createArgumentType(JsonParser jsonParser, JsonNode node) throws JsonParseException, InvalidTypeException {
        String type = node.get("type").textValue();
        String name = node.get("name").textValue();
        String defaultValue = node.has("default") ? node.get("default").textValue() : null;

        return switch (type) {
            case "Boolean" -> new BooleanType(name, defaultValue);
            case "Condition" -> new ConditionType(name);
            case "Dictionary" -> new DictionaryType(name);
            case "Keyword" -> new KeywordType(name);
            case "List" -> new ListType(name);
            case "Locator" -> new LocatorType(name);
            case "LogLevel" -> new LogLevelType(name, defaultValue);
            case "Number" -> new NumberType(name, defaultValue);
            case "Object" -> new ObjectType(name);
            case "Path" -> new PathType(name);
            case "String" -> new StringType(name, defaultValue);
            case "Timeout" -> new TimeoutType(name);
            default -> throw new JsonParseException(jsonParser, String.format("Invalid argument type: %s", type));
        };
    }
}
