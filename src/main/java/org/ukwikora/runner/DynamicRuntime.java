package org.ukwikora.runner;

import org.ukwikora.model.Keyword;
import org.ukwikora.model.Project;
import org.ukwikora.report.Report;

public class DynamicRuntime extends Runtime{
    private Report report;

    DynamicRuntime(Project project){
        super(project);
        this.report = new Report();
    }

    public Report getReport() {
        return report;
    }

    @Override
    public Keyword findKeyword(String library, String name) throws InstantiationException, IllegalAccessException {
        return null;
    }
}
