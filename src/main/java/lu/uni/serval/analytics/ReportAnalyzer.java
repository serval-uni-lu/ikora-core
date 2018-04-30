package lu.uni.serval.analytics;

import lu.uni.serval.robotframework.report.Report;
import lu.uni.serval.utils.KeywordData;
import lu.uni.serval.utils.tree.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReportAnalyzer {
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

    public Map<KeywordData, KeywordData> findDifferences(){
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

            }
        }

        for(EditAction difference: differences){
            System.out.println("[" + difference.operation.toString() + "] From :'" + difference.getNodeLabel1() + "' to '" + difference.getNodeLabel2() + "'");
        }

        return new HashMap<>();
    }
}
