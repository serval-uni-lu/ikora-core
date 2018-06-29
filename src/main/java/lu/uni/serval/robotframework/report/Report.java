package lu.uni.serval.robotframework.report;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    public List<Suite> getSuites() {
        return suites;
    }

    @Override
    public int getChildPosition(ReportElement element, boolean ignoreGhosts) {
        if(element instanceof Suite){
            return  Utils.getElementPosition(suites, (Suite) element, ignoreGhosts);
        }

        return -1;
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
