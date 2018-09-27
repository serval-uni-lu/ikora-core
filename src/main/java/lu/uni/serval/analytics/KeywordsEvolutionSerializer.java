package lu.uni.serval.analytics;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import lu.uni.serval.robotframework.model.*;
import lu.uni.serval.utils.Differentiable;

import java.io.IOException;

public class KeywordsEvolutionSerializer extends JsonSerializer<EvolutionResults> {
    private enum Position{
        First, Last, Both, Other
    }

    @Override
    public void serialize(EvolutionResults results, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException, JsonProcessingException {
        jsonGenerator.writeStartArray();
        int id = 1;

        for(TimeLine timeLine : results.getTimeLines()){
            if(!isKeywordDefinition(timeLine)){
                continue;
            }

            int size = timeLine.size();
            int loc = getLoc(timeLine);
            int changes = 0;

            for(int i = 0; i < size; ++i){
                Position position = getPosition(i, size);
                writeDifference(jsonGenerator, timeLine.get(i), position, id, loc, changes);
                changes += timeLine.get(i).getActions().size();
            }

            ++id;
        }

        jsonGenerator.writeEndArray();
    }

    private void writeDifference(JsonGenerator jsonGenerator, Difference difference, Position position, int id, int loc, int changes) throws IOException {
        switch (position){
            case First:
            case Other:
            {
                if(difference.getLeft() instanceof KeywordDefinition){
                    writeKeyword(jsonGenerator, (KeywordDefinition)difference.getLeft(), difference, id, loc, changes);
                }
            }
            break;

            case Last:
            {
                if(difference.getRight() instanceof KeywordDefinition){
                    Difference emptyDifference = Difference.of(difference.getRight(), difference.getRight());
                    changes += difference.getActions().size();
                    writeKeyword(jsonGenerator, (KeywordDefinition)difference.getRight(), emptyDifference, id, loc, changes + difference.getActions().size());
                }
            }
            break;

            case Both:
            {
                if(difference.getLeft() instanceof KeywordDefinition){
                    writeKeyword(jsonGenerator, (KeywordDefinition)difference.getLeft(), difference, id, loc, changes);
                }

                if(difference.getRight() instanceof KeywordDefinition){
                    Difference emptyDifference = Difference.of(difference.getRight(), difference.getRight());
                    changes += difference.getActions().size();
                    writeKeyword(jsonGenerator, (KeywordDefinition)difference.getRight(), emptyDifference, id, loc, changes);
                }
            }
            break;
        }
    }

    private void writeKeyword(JsonGenerator jsonGenerator, KeywordDefinition keyword, Difference difference, int id, int loc, int changes) throws IOException {
        jsonGenerator.writeStartObject();

        jsonGenerator.writeNumberField("type", getType(keyword));
        jsonGenerator.writeNumberField("time", keyword.getEpoch());
        jsonGenerator.writeNumberField("id", id);
        jsonGenerator.writeNumberField("number steps", keyword.getSteps().size());
        jsonGenerator.writeNumberField("sequence size", keyword.getMaxSequenceSize());
        jsonGenerator.writeNumberField("number branches", keyword.getBranchIndex());
        jsonGenerator.writeNumberField("size", keyword.getSize());
        jsonGenerator.writeNumberField("depth", keyword.getDepth());
        jsonGenerator.writeNumberField("connectivity", keyword.getConnectivity(-1));
        jsonGenerator.writeNumberField("churn", (float)changes / loc);

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

        differencesJson.writeJson(jsonGenerator, KeywordDefinition.class);
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

    private boolean isKeywordDefinition(TimeLine timeLine){
        if(timeLine.size() == 0){
            return false;
        }

        Difference difference = timeLine.get(0);
        Differentiable differentiable = difference.getValue();

        return KeywordDefinition.class.isAssignableFrom(differentiable.getClass());
    }

    private int getLoc(TimeLine timeLine){
        Differentiable differentiable = timeLine.get(0).getValue();
        return ((KeywordDefinition)differentiable).getSteps().size() + 1;
    }
}
