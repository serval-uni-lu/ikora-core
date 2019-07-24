package org.ukwikora.report;

import org.ukwikora.model.Keyword;

public class ReportBuilder {
    private Report report;
    private ReportNode currentNode;

    public ReportBuilder(){
        this.report = new Report();
    }

    public Report getReport() {
        return report;
    }

    public void enterKeyword(Keyword keyword) {
    }

    public void exitKeyword(Keyword keyword) {
    }
}
