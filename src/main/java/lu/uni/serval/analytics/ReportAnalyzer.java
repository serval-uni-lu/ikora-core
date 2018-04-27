package lu.uni.serval.analytics;

import lu.uni.serval.robotframework.report.Report;

import java.util.ArrayList;
import java.util.List;

public class ReportAnalyzer {
    private List<Report> reports;

    public ReportAnalyzer(){
        reports = new ArrayList<>();
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
    }
}
