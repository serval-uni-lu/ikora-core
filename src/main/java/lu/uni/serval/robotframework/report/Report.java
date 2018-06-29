package lu.uni.serval.robotframework.report;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import lu.uni.serval.utils.tree.LabelTreeNode;

public class Report implements ReportElement {
    private LocalDateTime creationTime;
    private String generator;
    private List<Suite> suites;

    public Report(){
        suites = new ArrayList<>();
    }

    public LocalDateTime  getCreationTime() {
        return creationTime;
    }

    public String getGenerator() {
        return generator;
    }

    public List<KeywordStatus> getKeywords(){
        List<KeywordStatus> keywords = new ArrayList<>();

        for(Suite suite: suites){
            keywords.addAll(suite.getKeywords());
        }

        return keywords;
    }

    @Override
    public ReportElement getParent() {
        return null;
    }

    @Override
    public ReportElement getRootElement() {
        return this;
    }

    @Override
    public String getSource() {
        return null;
    }

    public void setCreationTime(LocalDateTime  creationTime) {
        this.creationTime = creationTime;
    }

    public void setGenerator(String generator) {
        this.generator = generator;
    }

    public void addSuite(Suite suite) {
        suite.setParent(this);
        suites.add(suite);
    }
}
