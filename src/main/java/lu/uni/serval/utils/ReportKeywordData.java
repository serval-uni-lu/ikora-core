package lu.uni.serval.utils;

import lu.uni.serval.utils.tree.TreeNodeData;

import java.time.LocalDateTime;

public class ReportKeywordData extends KeywordData {
    public enum Status{
        UNDEFINED, PASS, FAILED
    }

    public String id;
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

    @Override
    public boolean isSame(TreeNodeData other){
        if(! this.getClass().equals(other.getClass())){
            return false;
        }

        ReportKeywordData data = (ReportKeywordData)other;

        return this.id.equals(data.id) &&
                this.name.equals(data.name) &&
                this.library.equals(data.library) &&
                this.arguments == data.arguments;
    }
}
