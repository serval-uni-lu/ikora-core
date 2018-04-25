package lu.uni.serval.robotframework.report;

import lu.uni.serval.utils.tree.TreeNode;

import java.util.ArrayList;
import java.util.List;

public class Suite {
    private String source;
    private String id;
    private String name;
    private List<Suite> children;
    private List<TreeNode> keywords;

    public Suite(){
        children = new ArrayList<>();
        keywords = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public String getSource() {
        return source;
    }

    public List<Suite> getChildren() {
        return children;
    }

    public List<TreeNode> getKeywords() {
        return keywords;
    }

    public boolean hasChildren(){
        return children.size() > 0;
    }

    public boolean hasKeywords(){
        return keywords.size() > 0;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public void setChildren(List<Suite> children) {
        this.children = children;
    }

    public void setKeywords(List<TreeNode> keywords) {
        this.keywords = keywords;
    }

    public void addSuite(Suite suite) {
        children.add(suite);
    }

    public void addKeyword(TreeNode keyword) {
        keywords.add(keyword);
    }
}
