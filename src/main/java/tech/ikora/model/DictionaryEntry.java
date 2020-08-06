package tech.ikora.model;

import tech.ikora.analytics.Edit;
import tech.ikora.analytics.visitor.NodeVisitor;
import tech.ikora.analytics.visitor.VisitorMemory;
import tech.ikora.runner.Runtime;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DictionaryEntry extends Value {
    private final SourceNode key;
    private final SourceNode value;

    public DictionaryEntry(SourceNode key, SourceNode value){
        addToken(key.getNameToken());
        addToken(value.getNameToken());

        this.key = key;
        this.value = value;
    }

    public SourceNode getKey(){
        return this.key;
    }

    public SourceNode getValue(){
        return this.value;
    }


    @Override
    public boolean matches(Token name) {
        return false;
    }

    @Override
    public void accept(NodeVisitor visitor, VisitorMemory memory) {

    }

    @Override
    public void execute(Runtime runtime) throws Exception {

    }

    @Override
    public Token getNameToken() {
        return getKey().getNameToken();
    }

    @Override
    public double distance(Differentiable other) {
        if(this == other){
            return 0.;
        }

        if(other == null){
            return 1.;
        }

        if(DictionaryEntry.class.isAssignableFrom(other.getClass())){
            double distance = this.getKey().distance(((DictionaryEntry)other).getKey()) / 2;
            distance += this.getValue().distance(((DictionaryEntry)other).getValue()) / 2;

            return distance;
        }

        return 1.;
    }

    @Override
    public List<Edit> differences(Differentiable other) {
        if(other == null){
            return Collections.singletonList(Edit.removeElement(DictionaryEntry.class, this));
        }

        if(other == this){
            return Collections.emptyList();
        }

        if(!(other instanceof DictionaryEntry)){
            return Collections.singletonList(Edit.changeValueType(this, other));
        }

        DictionaryEntry entry = (DictionaryEntry)other;

        List<Edit> edits = new ArrayList<>();
        edits.addAll(this.key.differences(entry.key));
        edits.addAll(this.value.differences(entry.value));

        return edits;
    }
}
