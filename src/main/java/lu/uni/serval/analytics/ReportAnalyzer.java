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
            List<LabelTreeNode> keywords = report.getKeywords();

            for(LabelTreeNode keyword: keywords){
                sequences.add(keyword);
            }
        }
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

    @Override
    public Iterator<Report> iterator() {
        return reports.iterator();
    }
}
