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

    KeywordDefinition(String name, List<String> arguments, String documentation){

        this.name = new Argument(name);
        this.documentation = documentation;

        this.arguments = new ArrayList<>();
        for(String argument : arguments) {
            this.arguments.add(new Argument(argument));
        }

        node = new LabelTreeNode(this);
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
