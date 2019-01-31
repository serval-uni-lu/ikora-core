package org.ukwikora.export.website;

import freemarker.template.*;
import org.ukwikora.model.Project;
import org.ukwikora.utils.FileUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class StatisticsViewerGenerator {
    private List<Project> projects;
    private File destination;

    public StatisticsViewerGenerator(List<Project> projects, File destination){
        this.projects = projects;
        this.destination = destination;
    }

    public void generate() throws IOException, URISyntaxException, TemplateException {
        copyResources();
        generateSummaryPage();
    }

    void copyResources() throws IOException, URISyntaxException {
        File source = FileUtils.getResourceFile("reporting/html/project_analytics");
        org.apache.commons.io.FileUtils.copyDirectory(source, destination);
    }

    void generateSummaryPage() throws IOException, URISyntaxException, TemplateException {
        ProjectsSummary summary = new ProjectsSummary(projects);

        Map<String, Object> input = new HashMap<>();
        input.put("summary", summary);

        processTemplate("summary.ftl", input, new File(destination, "index.html"));
        processTemplate("summary-loc.ftl", input, new File(destination, "js/summary-loc.js"));
        processTemplate("summary-user-keywords.ftl", input, new File(destination, "js/summary-user-keywords.js"));
        processTemplate("summary-test-cases.ftl", input, new File(destination, "js/summary-test-cases.js"));
    }

    Template getTemplate(String name) throws IOException, URISyntaxException {
        Configuration cfg = new Configuration();

        File templateDirectory = FileUtils.getResourceFile("reporting/html/ftl");
        cfg.setDirectoryForTemplateLoading(templateDirectory);

        cfg.setIncompatibleImprovements(new Version(2, 3, 20));
        cfg.setDefaultEncoding("UTF-8");
        cfg.setLocale(Locale.US);
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);

        return cfg.getTemplate(name);
    }

    void processTemplate(String name, Map<String, Object> input, File output) throws IOException, URISyntaxException, TemplateException {
        try(Writer fileWriter = new FileWriter(output)) {
            Template template = getTemplate(name);
            template.process(input, fileWriter);
        }
    }
}
