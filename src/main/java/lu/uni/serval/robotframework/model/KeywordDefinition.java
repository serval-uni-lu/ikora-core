package lu.uni.serval.robotframework.model;

import lu.uni.serval.utils.tree.LabelTreeNode;
import lu.uni.serval.utils.tree.TreeNodeData;

import javax.annotation.Nonnull;
import java.util.*;

public class KeywordDefinition implements Keyword, Iterable<Step> {
    private String file;
    private Argument name;
    private String documentation;
    private List<String> tags;
    private LabelTreeNode node;
    private List<Step> steps;
    private Set<Keyword> dependencies;

    KeywordDefinition(){
        steps = new ArrayList<>();
        node = new LabelTreeNode(this);
        dependencies = new HashSet<>();
        tags = new ArrayList<>();
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

    public void addTag(String tag){
        this.tags.add(tag);
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


    public List<String> getTags() {
        return tags;
    }

    @Override
    public LabelTreeNode getNode() {
        return node;
    }

    @Override
    public Set<Keyword> getDependencies() {
        return dependencies;
    }

    @Override
    public void addDependency(Keyword keyword) {
        this.dependencies.add(keyword);
    }

    public boolean matches(String name) {
        return this.name.matches(name);
    }

    @Override
    public String getLabel() {
        return getName().toString();
    }

    @Override
    public boolean isSame(TreeNodeData other) {
        return this == other;
    }

    @Override
    public boolean isValid() {
        return true;
    }

    @Nonnull
    public Iterator<Step> iterator() {
        return steps.iterator();
    }
}
