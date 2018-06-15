package lu.uni.serval.robotframework.model;

import lu.uni.serval.utils.tree.LabelTreeNode;
import lu.uni.serval.utils.tree.TreeNodeData;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class KeywordDefinition implements TreeNodeData, Iterable<Step> {
    protected String file;
    protected Argument name;
    protected String documentation;
    protected LabelTreeNode node;
    protected List<Step> steps;
    protected List<KeywordDefinition> dependencies;

    KeywordDefinition(){
        steps = new ArrayList<>();
        node = new LabelTreeNode(this);
        dependencies = new ArrayList<>();
    }

    public void setFile(String file){
        this.file = file;
    }

    public void setName(String name){
        this.name = new Argument(name);
    }

    public void addStep(Step step){
        this.steps.add(step);
        step.setParent(this);
    }

    public void addDependency(KeywordDefinition keyword) {
        this.dependencies.add(keyword);
    }

    public String getFile() {
        return file;
    }

    public List<Step> getSteps() {
        return steps;
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

    public LabelTreeNode getNode() {
        return node;
    }

    public boolean isResolved(String name) {
        return this.name.matches(name);
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

    @Nonnull
    public Iterator<Step> iterator() {
        return steps.iterator();
    }
}
