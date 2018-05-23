package lu.uni.serval.analytics;


import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import lu.uni.serval.utils.CompareCache;
import lu.uni.serval.utils.EasyPair;
import lu.uni.serval.utils.KeywordData;
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
        jsonGenerator.writeStartObject();

        jsonGenerator.writeStringField("keyword threshold", String.valueOf(CloneIndex.getKeywordThreshold()));
        jsonGenerator.writeStringField("tree threshold", String.valueOf(CloneIndex.getTreeThreshold()));

        for (CloneResults.CloneType type : CloneResults.CloneType.values()) {
            writeJsonClones(jsonGenerator, type.name(), cloneResults.getByType(type));
        }

        jsonGenerator.writeEndObject();
    }

    private void writeJsonClones(JsonGenerator jsonGenerator, String name, CompareCache<LabelTreeNode, CloneIndex> clones) throws IOException {
        if(clones == null){
            return;
        }

        jsonGenerator.writeObjectFieldStart(name);
        jsonGenerator.writeStringField("size", String.valueOf(clones.size()));

        jsonGenerator.writeArrayFieldStart("data");
        for(Map.Entry<UnorderedPair<LabelTreeNode>, CloneIndex> clone: clones){
            writeJsonClone(clone.getKey(), clone.getValue(), jsonGenerator);
        }
        jsonGenerator.writeEndArray();
        jsonGenerator.writeEndObject();
    }

    private void writeJsonClone(UnorderedPair<LabelTreeNode> treePair, CloneIndex index, JsonGenerator jsonGenerator) throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeNumberField("keyword index", index.getKeywordRatio());
        jsonGenerator.writeNumberField("tree index", index.getTreeRatio());
        jsonGenerator.writeNumberField("semantic index", index.getSemanticRatio());
        jsonGenerator.writeArrayFieldStart("tree pair");
        writeJsonTree(treePair.first(), jsonGenerator);
        writeJsonTree(treePair.second(), jsonGenerator);
        jsonGenerator.writeEndArray();
        jsonGenerator.writeEndObject();
    }

    private void writeJsonTree(LabelTreeNode tree, JsonGenerator jsonGenerator) throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeStringField("file", ((KeywordData)tree.getData()).file);
        jsonGenerator.writeStringField("name", ((KeywordData)tree.getData()).name);
        jsonGenerator.writeNumberField("number of steps", tree.getChildCount());
        jsonGenerator.writeEndObject();
    }
}
