package lu.uni.serval.robotframework.report;

import lu.uni.serval.robotframework.model.Keyword;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class KeywordStatus {

    public enum Status{
        PASS, FAIL, IGNORED
    }

    private Status status;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private KeywordStatus parent;
    private Keyword keyword;
    private String log;

    private List<KeywordStatus> children;

    public KeywordStatus() {
        this.status = Status.IGNORED;
        this.keyword = null;

        this.children = new ArrayList<>();
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
        children.add(child);
    }

    public void setParent(KeywordStatus parent) {
        this.parent = parent;
    }

    public Keyword getKeyword() {
        return this.keyword;
    }
}
