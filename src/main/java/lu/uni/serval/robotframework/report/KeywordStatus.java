package lu.uni.serval.robotframework.report;

import lu.uni.serval.robotframework.model.Keyword;
import lu.uni.serval.robotframework.model.Step;
import lu.uni.serval.robotframework.model.TestCase;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class KeywordStatus implements ReportElement {
    public enum Status{
        PASS, FAIL, IGNORED
    }

    public enum Type{
        TESTCASE, STEP, INVALID
    }

    private Status status;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private ReportElement parent;
    private Keyword keyword;
    private String log;

    private String name;
    private List<String> arguments;

    private List<KeywordStatus> children;

    public KeywordStatus() {
        this.status = Status.IGNORED;
        this.keyword = null;

        this.arguments = new ArrayList<>();
        this.children = new ArrayList<>();
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addArgument(String argument) {
        this.arguments.add(argument);
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void setStatus(String str) {
        if(str.equalsIgnoreCase("PASS") || str.equalsIgnoreCase("PASSED")) {
            setStatus(Status.PASS);
        }
        else if (str.equalsIgnoreCase("FAIL") || str.equalsIgnoreCase("FAILED")) {
            setStatus(Status.FAIL);
        }
        else {
            setStatus(Status.IGNORED);
        }
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public void setKeyword(Keyword keyword) {
        this.keyword = keyword;
    }

    public void setLog(String log) {
        this.log = log;
    }

    public void addChild(KeywordStatus child) {
        child.setParent(this);
        children.add(child);
    }

    public void setParent(ReportElement parent) {
        this.parent = parent;
    }

    public Status getStatus() {
        return status;
    }

    public List<KeywordStatus> getChildren() {
        return children;
    }

    public Keyword getKeyword() {
        return this.keyword;
    }

    public Type getType() {
        if(keyword == null) {
            return Type.INVALID;
        }
        else if(keyword instanceof TestCase) {
            return Type.TESTCASE;
        }
        else if(keyword instanceof Step) {
            return Type.STEP;
        }

        return Type.INVALID;
    }

    public String getLog() {
        return log;
    }

    public String getName(){
        return name;
    }

    public int getStepPosition() {
        if(parent == null){
            return -1;
        }

        return parent.getChildPosition(this);
    }

    @Override
    public String getSource() {
        if(parent == null) {
            return null;
        }

        return parent.getSource();
    }

    @Override
    public ReportElement getParent() {
        return parent;
    }

    @Override
    public ReportElement getRootElement() {
        if(parent == null){
            return null;
        }

        return parent.getRootElement();
    }

    @Override
    public int getChildPosition(ReportElement element) {
        for(int i = 0; i < children.size(); ++i){
            if(children.get(i) == element){
                return i;
            }
        }

        return -1;
    }
}
