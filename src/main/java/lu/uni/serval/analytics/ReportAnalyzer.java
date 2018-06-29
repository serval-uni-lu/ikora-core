package lu.uni.serval.analytics;

import lu.uni.serval.robotframework.report.KeywordStatus;
import lu.uni.serval.robotframework.report.Report;
import lu.uni.serval.utils.tree.*;

import javax.annotation.Nonnull;
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
        TreeEditDistance editDistance = new TreeEditDistance(1.0, 1.0, 0.8);

        DifferenceMemory memory = new DifferenceMemory();

        for(List<KeywordStatus> sequence: sequences){
            KeywordStatus previous = null;
            for(KeywordStatus keyword: sequence){
                if(previous == null){
                    previous = keyword;
                    continue;
                }
                /*
                LocalDateTime dateTime = KeywordStatus.getExecutionDate();

                for(EditAction difference : editDistance.differences(previous, keyword)){
                    // check that the change was not already accounted (keywords are reused)
                    if(memory.addDifference(dateTime, difference)){
                        differences.addDifference(difference);
                    }

                }
                */

                previous = keyword;
            }
        }

        return differences;
    }

    private void initKeywordSequence(){
        if(sequences != null){
            return;
        }

        initStatus();

        sequences = new KeywordSequence();

        for(Report report: reports){
            List<KeywordStatus> keywords = report.getKeywords();

            for(KeywordStatus keyword: keywords){
                //if(!status.isServiceDown(keyword)){
                    sequences.add(keyword);
                //}
            }
        }
    }

    private void initStatus(){
        if(status != null){
            return;
        }

        status = new StatusResults();

        for(Report report: reports){
            List<KeywordStatus> keywords = report.getKeywords();

            for(KeywordStatus keyword: keywords){
                status.add(keyword);
            }
        }
    }

    @Override
    @Nonnull
    public Iterator<Report> iterator() {
        return reports.iterator();
    }

}
