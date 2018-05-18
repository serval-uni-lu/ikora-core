package lu.uni.serval.robotframework.report;

import java.util.ArrayList;
import java.util.List;

import lu.uni.serval.utils.tree.LabelTreeNode;

public class Suite {
    private String source;
    private String id;
    private String name;
    private List<Suite> suites;
    private List<LabelTreeNode> keywords;

    public Suite(){
        suites = new ArrayList<>();
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
        return suites;
    }

    public List<LabelTreeNode> getKeywords() {
        List<LabelTreeNode> keywords = new ArrayList<>();

        if(hasSuites()){
            for(Suite suite: this.suites){
                keywords.addAll(suite.getKeywords());
            }
        }

        if(hasKeywords()){
            keywords.addAll(this.keywords);
        }

        return keywords;
    }

    public boolean hasSuites(){
        return suites.size() > 0;
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
        this.suites = children;
    }

    public void setKeywords(List<LabelTreeNode> keywords) {
        this.keywords = keywords;
    }

    public void addSuite(Suite suite) {
        suites.add(suite);
    }

    public void addKeyword(LabelTreeNode keyword) {
        keywords.add(keyword);
    }
}
