package lu.uni.serval.analytics;

import lu.uni.serval.robotframework.report.Report;
import lu.uni.serval.utils.tree.*;

import java.util.*;

public class ReportAnalyzer implements Iterable<Report>{
    private List<Report> reports;
    private KeywordSequence sequences;

    public ReportAnalyzer(){
        reports = new ArrayList<>();
        sequences = null;
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
    }

    private void initKeywordSequence(){
        if(sequences != null){
            return;
        }

        sequences = new KeywordSequence();

        for(Report report: reports){
            List<TreeNode> keywords = report.getKeywords();

            for(TreeNode keyword: keywords){
                sequences.add(keyword);
            }
        }
    }

    public List<EditAction> findDifferences(){
        initKeywordSequence();

        TreeEditDistance editDistance = new TreeEditDistance(new CloneEditScore());

        List<EditAction> differences = new ArrayList<>();

        for(List<TreeNode> sequence: sequences){

            TreeNode previous = null;
            for(TreeNode keyword: sequence){
                if(previous == null){
                    previous = keyword;
                    continue;
                }

                differences.addAll(editDistance.differences(previous, keyword));
                previous = keyword;
            }
        }

        return differences;
    }

    @Override
    public Iterator<Report> iterator() {
        return reports.iterator();
    }
}
