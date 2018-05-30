package lu.uni.serval.analytics;

import lu.uni.serval.robotframework.report.Report;
import lu.uni.serval.utils.ReportKeywordData;
import lu.uni.serval.utils.tree.*;

import java.time.LocalDateTime;
import java.util.*;

public class ReportAnalyzer implements Iterable<Report>{
    private List<Report> reports;
    private KeywordSequence sequences;
    private StatusResults status;

    public ReportAnalyzer(){
        reports = new ArrayList<>();
        sequences = null;
    }

    public StatusResults getStatus(){
        initStatus();
        return status;
    }

    public void add(Report report){
        boolean inserted = false;

        for(int index = 0; index < reports.size(); ++index){
            if(reports.get(index).getCreationTime().isAfter(report.getCreationTime())){
                reports.add(index, report);
                inserted = true;
                break;
            }
        }

        if(!inserted){
            reports.add(report);
        }

        sequences = null;
        status = null;
    }


    public DifferenceResults findDifferences(){
        initKeywordSequence();

        DifferenceResults differences = new DifferenceResults();
        TreeEditDistance editDistance = new TreeEditDistance(1.0, 1.0, 1.0);

        for(List<LabelTreeNode> sequence: sequences){
            LabelTreeNode previous = null;
            for(LabelTreeNode keyword: sequence){
                if(previous == null){
                    previous = keyword;
                    continue;
                }

                for(EditAction difference : editDistance.differences(previous, keyword)){
                    differences.addDifference(difference);
                }

                previous = keyword;
            }
        }

        return differences;
    }

    private void initKeywordSequence(){
        if(sequences != null){
            return;
        }

        sequences = new KeywordSequence();

        for(Report report: reports){
            List<LabelTreeNode> keywords = report.getKeywords();

            for(LabelTreeNode keyword: keywords){
                if(!isServiceDown(keyword)){
                    sequences.add(keyword);
                }
            }
        }
    }

    private void initStatus(){
        if(status != null){
            return;
        }

        status = new StatusResults();

        for(Report report: reports){
            List<LabelTreeNode> keywords = report.getKeywords();

            for(LabelTreeNode keyword: keywords){
                status.add(keyword);
            }
        }
    }

    private boolean isServiceDown(LabelTreeNode keyword) {
        if(keyword.getData() instanceof ReportKeywordData){
            initStatus();

            LocalDateTime executionDate = ((ReportKeywordData)keyword.getData()).executionDate;
            return status.getFailureRate(executionDate) > 0.5;
        }

        return false;
    }

    @Override
    public Iterator<Report> iterator() {
        return reports.iterator();
    }

}
