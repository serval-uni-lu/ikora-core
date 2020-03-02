package tech.ikora.model;

import tech.ikora.analytics.Action;
import tech.ikora.analytics.visitor.NodeVisitor;
import tech.ikora.analytics.visitor.VisitorMemory;
import tech.ikora.builder.ValueLinker;
import tech.ikora.exception.InvalidArgumentException;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class DictionaryVariable extends Variable {
    private List<DictionaryEntry> entries;

    public DictionaryVariable(Token name){
        super(name);
        entries = new ArrayList<>();
    }

    @Override
    public void addElement(Node value) throws InvalidArgumentException {

    }

    public void setEntries(List<DictionaryEntry> entries){
        this.entries = entries;
    }

    @Override
    public double distance(Differentiable other) {
        return 0;
    }

    @Override
    public List<Action> differences(Differentiable other) {
        return null;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(name.getText());

        for(DictionaryEntry entry: entries){
            builder.append("\t");
            builder.append(entry.toString());
        }

        return builder.toString();
    }

    @Override
    public boolean isDeadCode(){
        return getDependencies().size() == 0;
    }

    @Override
    public void accept(NodeVisitor visitor, VisitorMemory memory){
        visitor.visit(this, memory);
    }

    @Override
    protected void setName(Token name) {
        this.name = name;

        String generic = ValueLinker.escape(ValueLinker.getGenericVariableName(this.name.getText()));
        this.pattern = Pattern.compile(generic, Pattern.CASE_INSENSITIVE);
    }
}
