package lu.uni.serval.robotframework.model;

import lu.uni.serval.utils.tree.LabelTreeNode;
import lu.uni.serval.utils.tree.TreeNodeData;

import java.util.ArrayList;
import java.util.List;

public class KeywordDefinition implements TreeNodeData {
    protected List<Argument> arguments;
    protected Argument name;
    protected String documentation;
    protected LabelTreeNode node;

    KeywordDefinition(){
        arguments = new ArrayList<>();
        node = new LabelTreeNode(this);
    }

    public void setName(String name){
        this.name = new Argument(name);
    }

    public void addArgument(String argument){
        arguments.add(new Argument(argument));
    }

    public void setDocumentation(String documentation){
        this.documentation = documentation;
    }

    public Argument getName() {
        return name;
    }

    public String getDocumentation() {
        return documentation;
    }

    public List<Argument> getArguments() {
        return arguments;
    }

    @Override
    public String getLabel() {
        return null;
    }

    @Override
    public boolean isSame(TreeNodeData other) {
        return false;
    }

    @Override
    public boolean isValid() {
        return false;
    }
}
