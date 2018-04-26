package lu.uni.serval.robotframework.report;

import lu.uni.serval.utils.tree.TreeNode;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Report {
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

    public List<TreeNode> getKeywords(){
        List<TreeNode> keywords = new ArrayList<>();

        for(Suite suite: suites){
            keywords.addAll(suite.getKeywords());
        }

        return keywords;
    }


    public void setCreationTime(LocalDateTime  creationTime) {
        this.creationTime = creationTime;
    }

    public void setGenerator(String generator) {
        this.generator = generator;
    }

    public void addSuite(Suite suite) {
        suites.add(suite);
    }
}
