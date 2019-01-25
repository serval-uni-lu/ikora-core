package org.ukwikora.export.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.ukwikora.analytics.*;
import org.ukwikora.model.Statement;
import org.ukwikora.model.UserKeyword;
import org.ukwikora.utils.StringUtils;
import org.ukwikora.model.KeywordDefinition;
import org.ukwikora.model.TestCase;

import java.io.IOException;

public class KeywordsEvolutionSerializer extends JsonSerializer<EvolutionResults> {
    private enum Position{
        First, Last, Both, Other
    }

    @Override
    public void serialize(EvolutionResults results, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartArray();
        int id = 1;

        for(TimeLine timeLine : results.getTimeLines()){
            if(!timeLine.isKeywordDefinition()){
                continue;
            }

            int size = timeLine.size();

            for(int i = 0; i < size; ++i){
                Position position = getPosition(i, size);
                writeDifference(jsonGenerator, timeLine.get(i), position, id);
            }

            ++id;
        }

        jsonGenerator.writeEndArray();
    }

    private void writeDifference(JsonGenerator jsonGenerator, Difference difference, Position position, int id) throws IOException {
        switch (position){
            case First:
            case Other:
            {
                if(difference.getLeft() instanceof KeywordDefinition){
                    writeKeyword(jsonGenerator, (KeywordDefinition)difference.getLeft(), difference, id);
                }
            }
            break;

            case Last:
            {
                if(difference.getRight() instanceof KeywordDefinition){
                    Difference emptyDifference = Difference.of(difference.getRight(), difference.getRight());
                    writeKeyword(jsonGenerator, (KeywordDefinition)difference.getRight(), emptyDifference, id);
                }
            }
            break;

            case Both:
            {
                if(difference.getLeft() instanceof KeywordDefinition){
                    writeKeyword(jsonGenerator, (KeywordDefinition)difference.getLeft(), difference, id);
                }

                if(difference.getRight() instanceof KeywordDefinition){
                    Difference emptyDifference = Difference.of(difference.getRight(), difference.getRight());
                    writeKeyword(jsonGenerator, (KeywordDefinition)difference.getRight(), emptyDifference, id);
                }
            }
            break;
        }
    }

    private void writeKeyword(JsonGenerator jsonGenerator, KeywordDefinition keyword, Difference difference, int id) throws IOException {
        jsonGenerator.writeStartObject();

        jsonGenerator.writeNumberField("type", getType(keyword));
        jsonGenerator.writeNumberField("time", keyword.getEpoch());
        jsonGenerator.writeNumberField("id", id);
        jsonGenerator.writeNumberField("lines of code", keyword.getLoc());
        jsonGenerator.writeNumberField("document length", getDocumentationLength(keyword));
        jsonGenerator.writeNumberField("number steps", keyword.getSteps().size());
        jsonGenerator.writeNumberField("sequence size", keyword.getMaxSequenceSize());
        jsonGenerator.writeNumberField("number branches", keyword.getBranchIndex());
        jsonGenerator.writeNumberField("size", keyword.getSize());
        jsonGenerator.writeNumberField("depth", keyword.getLevel());
        jsonGenerator.writeNumberField("connectivity", getConnectivity(keyword));

        writeChanges(jsonGenerator, difference);

        jsonGenerator.writeEndObject();
    }

    private int getType(KeywordDefinition keyword) {
        int type = -1;

        if(keyword instanceof UserKeyword){
            type = 0;
        }
        else if(keyword instanceof TestCase){
            type = 1;
        }
        return type;
    }

    private void writeChanges(JsonGenerator jsonGenerator, Difference difference) throws IOException {
        DifferencesJson differencesJson = new DifferencesJson();
        differencesJson.add(difference);

        differencesJson.writeJson(jsonGenerator);
    }

    private Position getPosition(int index, int size){
        Position position = Position.Other;

        if(size - 1 == 0 && index == 0){
            position = Position.Both;
        }
        else if(index == 0){
            position = Position.First;
        }
        else if(index == size - 1){
            position = Position.Last;
        }

        return position;
    }

    private int getDocumentationLength(KeywordDefinition keyword){
        String documentation = keyword.getDocumentation();

        if(documentation.isEmpty()){
            return 0;
        }

        return StringUtils.countLines(keyword.getDocumentation());
    }

    private int getConnectivity(Statement statement){
        ConnectivityVisitor visitor = new ConnectivityVisitor();
        statement.accept(visitor);

        return visitor.getConnectivity();
    }
}
