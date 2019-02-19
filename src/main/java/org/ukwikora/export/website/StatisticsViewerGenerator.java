package org.ukwikora.export.website;

import freemarker.template.*;
import org.ukwikora.export.website.model.SideBar;
import org.ukwikora.export.website.model.Summary;
import org.ukwikora.model.Project;
import org.ukwikora.utils.FileUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.*;

public class StatisticsViewerGenerator {
    private List<Project> projects;
    private File destination;

    public StatisticsViewerGenerator(List<Project> projects, File destination){
        this.projects = projects;
        this.destination = destination;
    }

    public void generate() throws Exception {
        SideBar sideBar = new SideBar(projects);
        Map<String, Object> input = new HashMap<>();
        input.put("sidebar", sideBar);

        copyResources();
        generateSummaryPage(new HashMap<>(input));
    }

    private void copyResources() throws Exception {
        File source = FileUtils.getResourceFile("reporting/html/project_analytics");
        org.apache.commons.io.FileUtils.copyDirectory(source, destination);
    }

    private void generateSummaryPage(Map<String, Object> input) throws Exception {
        Summary summary = new Summary(projects);

        input.put("summary", summary);

        processTemplate("summary.ftl", input, new File(destination, "index.html"));

        processTemplate("lib/bar-chart.ftl", Collections.singletonMap("chart", summary.getLinesChart()),
                new File(destination, summary.getLinesChart().getUrl()));

        processTemplate("lib/bar-chart.ftl",  Collections.singletonMap("chart", summary.getUserKeywordsChart()),
                new File(destination, summary.getUserKeywordsChart().getUrl()));

        processTemplate("lib/bar-chart.ftl", Collections.singletonMap("chart", summary.getTestCasesChart()),
                new File(destination, summary.getUserKeywordsChart().getUrl()));
    }

    private Template getTemplate(String name) throws Exception {
        Configuration cfg = new Configuration();

        File templateDirectory = FileUtils.getResourceFile("reporting/html/ftl");
        cfg.setDirectoryForTemplateLoading(templateDirectory);

        cfg.setIncompatibleImprovements(new Version(2, 3, 20));
        cfg.setDefaultEncoding("UTF-8");
        cfg.setLocale(Locale.US);
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);

        return cfg.getTemplate(name);
    }

    private void processTemplate(String name, Map<String, Object> input, File output) throws Exception {
        try(Writer fileWriter = new FileWriter(output)) {
            Template template = getTemplate(name);
            template.process(input, fileWriter);
        }
    }
}
