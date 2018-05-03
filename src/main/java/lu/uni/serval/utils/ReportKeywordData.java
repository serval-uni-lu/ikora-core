package lu.uni.serval.utils;

import java.time.LocalDateTime;

public class ReportKeywordData extends KeywordData {
    public enum Status{
        UNDEFINED, PASS, FAILED
    }

    public Status status;
    public LocalDateTime executionDate;

    public ReportKeywordData(){
        this.status = Status.UNDEFINED;
    }

    public void setStatus(String status) {
        if(status.equalsIgnoreCase("PASS")){
            this.status = Status.PASS;
        }
        else if(status.equalsIgnoreCase("FAIL")){
            this.status = Status.FAILED;
        }
        else{
            this.status = Status.UNDEFINED;
        }
    }
}
