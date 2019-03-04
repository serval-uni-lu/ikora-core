package org.ukwikora.export.website;

import freemarker.template.*;
import org.ukwikora.export.website.model.*;
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
        generateDependenciesPage(new HashMap<>(input));
        generateDeadCodePage(new HashMap<>(input));

        for(Project project: projects){
            generateSingleProjectPage(project, new HashMap<>(input));
        }
    }

    void copyResources() throws Exception {
        File source = FileUtils.getResourceFile("reporting/html/project_analytics");
        org.apache.commons.io.FileUtils.copyDirectory(source, destination);
    }

    void generateSummaryPage(Map<String, Object> input) throws Exception {
        Summary summary = new Summary(projects);

        input.put("summary", summary);
        processTemplate("summary.ftl", input, new File(destination, "index.html"));

        processTemplate("lib/bar-chart.ftl", Collections.singletonMap("chart", summary.getLinesChart()),
                new File(destination, summary.getLinesChart().getUrl()));

        processTemplate("lib/bar-chart.ftl",  Collections.singletonMap("chart", summary.getUserKeywordsChart()),
                new File(destination, summary.getUserKeywordsChart().getUrl()));

        processTemplate("lib/bar-chart.ftl", Collections.singletonMap("chart", summary.getTestCasesChart()),
                new File(destination, summary.getTestCasesChart().getUrl()));
    }

    void generateDependenciesPage(Map<String, Object> input) throws Exception {
        DependencyGraph dependencies = new DependencyGraph("projects-dependency-graph",
                "Dependency Graph", projects);

        input.put("dependencies", dependencies);
        input.put("dependencyGraphUrl", dependencies.getUrl());

        processTemplate("dependencies.ftl", input, new File(destination, "dependencies.html"));

        processTemplate("lib/dependency-graph.ftl", Collections.singletonMap("dependencies", dependencies),
                new File(destination, dependencies.getUrl()));
    }

    void generateDeadCodePage(Map<String, Object> input) throws Exception {
        DeadCode deadCode = new DeadCode("dead-code", "Dead Code", projects);

        input.put("deadCode", deadCode);

        processTemplate("dead-code.ftl", input, new File(destination, "dead-code.html"));
    }

    void generateSingleProjectPage(Project project, Map<String, Object> input) throws Exception {
        SingleProject singleProject = new SingleProject(project);

        input.put("project", singleProject);

        processTemplate("project.ftl", input, new File(destination, singleProject.getLink().getUrl()));

        processTemplate("lib/bar-chart.ftl", Collections.singletonMap("chart", singleProject.getConnectivityChart()),
                new File(destination, singleProject.getConnectivityChart().getUrl()));

        processTemplate("lib/bar-chart.ftl", Collections.singletonMap("chart", singleProject.getSizeChart()),
                new File(destination, singleProject.getSizeChart().getUrl()));

        processTemplate("lib/bar-chart.ftl", Collections.singletonMap("chart", singleProject.getDepthChart()),
                new File(destination, singleProject.getDepthChart().getUrl()));

        processTemplate("lib/bar-chart.ftl", Collections.singletonMap("chart", singleProject.getSequenceChart()),
                new File(destination, singleProject.getSequenceChart().getUrl()));
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
