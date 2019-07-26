package org.ukwikora.report;

import org.ukwikora.model.Keyword;
import org.ukwikora.utils.Globals;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Stack;

public class ReportBuilder {
    private Report report;
    private Stack<ReportElement> stack;

    public ReportBuilder(){
        this.report = new Report();
    }

    public Optional<Report> getReport() {
        return Optional.ofNullable(report);
    }

    public void enterKeyword(Keyword keyword) {
    }

    public void exitKeyword(Keyword keyword) {
    }

    public void reset() {
        report = new Report();
        report.setGenerator(Globals.applicationCanonical);

        stack = new Stack<>();
        stack.push(report);
    }

    public void finish(){
        report.setGenerated(LocalDateTime.now());
    }
}
