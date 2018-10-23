package lu.uni.serval.robotframework.analytics;

import lu.uni.serval.robotframework.report.Report;
import lu.uni.serval.robotframework.utils.CompareCache;
import opennlp.tools.util.StringUtil;

import javax.annotation.Nonnull;
import java.util.*;

import static org.apache.commons.lang3.math.NumberUtils.min;

public class ReportAnalyzer implements Iterable<Report>{
    private List<Report> reports;
    private TimeLine sequences;
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


    public EvolutionResults findDifferences(){
        initKeywordSequence();

        EvolutionResults differences = new EvolutionResults();
/*
        for(List<ElementInfo<KeywordDefinition>> sequence: sequences){
            ElementInfo<KeywordDefinition> previous = null;
            for(ElementInfo<KeywordDefinition> current: sequence){
                if(previous == null){
                    previous = current;
                    continue;
                }

                KeywordDefinition keyword1 = previous.getElement();
                KeywordDefinition keyword2 = current.getElement();

                Project project1 = previous.getProject();
                Project project2 = current.getProject();

                if(differences.containsElement(project1, project2, keyword1, keyword2)){
                    previous = current;
                    continue;
                }

                Difference difference = Difference.of(previous.getElement(), current.getElement());


                LocalDateTime dateTime = KeywordStatus.getExecutionDate();

                for(EditAction difference : editDistance.differences(previous, keyword)){
                    // check that the change was not already accounted (keywords are reused)
                    if(memory.addDifference(dateTime, difference)){
                        differences.addDifference(difference);
                    }

                }


                previous = current;
            }
        }
*/
        return differences;
    }

    private void initKeywordSequence(){
        if(sequences != null){
            return;
        }

        initStatus();

        sequences = new TimeLine();

        for(Report report: reports){
/*
            List<KeywordStatus> keywords = report.getKeywords();

            for(KeywordStatus keyword: keywords){
                if(!status.isServiceDown(keyword)){
                    sequences.add(keyword);
                }
            }
*/
        }
    }

    private void initStatus(){
        if(status != null){
            return;
        }

        status = new StatusResults();
/*
        for(Report report: reports){
            List<KeywordStatus> keywords = report.getKeywords();

            for(KeywordStatus keyword: keywords){
                status.add(keyword);
            }
        }
*/
    }

    @Override
    @Nonnull
    public Iterator<Report> iterator() {
        return reports.iterator();
    }
}
