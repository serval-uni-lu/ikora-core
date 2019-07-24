package org.ukwikora.runner;

import org.ukwikora.model.Keyword;
import org.ukwikora.model.Project;
import org.ukwikora.report.Report;
import org.ukwikora.report.ReportBuilder;

public class DynamicRuntime extends Runtime{
    private ReportBuilder reportBuilder;

    DynamicRuntime(Project project){
        super(project);
        this.reportBuilder = new ReportBuilder();
    }

    public Report getReport() {
        return reportBuilder.getReport();
    }

    @Override
    public Keyword findKeyword(String library, String name) throws InstantiationException, IllegalAccessException {
        return null;
    }

    @Override
    public void enterKeyword(Keyword keyword){
        this.reportBuilder.enterKeyword(keyword);
    }

    @Override
    public void exitKeyword(Keyword keyword){
        this.reportBuilder.exitKeyword(keyword);
    }
}
