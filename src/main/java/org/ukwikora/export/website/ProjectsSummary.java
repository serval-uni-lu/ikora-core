package org.ukwikora.export.website;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.ukwikora.model.Project;
import org.ukwikora.utils.JsonUtils;
import org.ukwikora.utils.StringUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class ProjectsSummary {
    private static Logger logger = LogManager.getLogger(ProjectsSummary.class);

    private List<Link> links;
    private List<String> labels;
    private List<Integer> lines;
    private List<Integer> userKeywords;
    private List<Integer> testCases;

    public ProjectsSummary(List<Project> projects){
        try {
            int size = projects.size();

            links = new ArrayList<>(size);
            labels = new ArrayList<>(size);
            lines = new ArrayList<>(size);
            userKeywords = new ArrayList<>(size);
            testCases = new ArrayList<>(size);

            for(Project project: projects){
                String name = project.getName();

                links.add(new Link(name));
                labels.add(StringUtils.toBeautifulName(name));
                lines.add(project.getLoc());
                userKeywords.add(project.getUserKeywords().size());
                testCases.add(project.getTestCases().size());
            }
        } catch (UnsupportedEncodingException e) {
            logger.error(String.format("Failed to retrieve data from projects to build ProjectSummary: %s", e.getMessage()));
        }
    }

    public List<Link> getLinks(){
        return links;
    }

    public List<Integer> getLines(){
        return lines;
    }

    public String getJsonLabels(){
        return getJsonArray("labels", labels);
    }

    public String getJsonLines(){
        return getJsonArray("lines", lines);
    }

    public String getJsonUserKeywords(){
        return getJsonArray("user keywords", userKeywords);
    }

    public String getJsonTestCases(){
        return getJsonArray("test cases", testCases);
    }

    private String getJsonArray(String name, List<?> list){
        try {
            return JsonUtils.convertToJsonArray(list);
        } catch (IOException e) {
            logger.error(String.format("Failed to get json %s for project summary: %s", name, e.getMessage()));
            return "[]";
        }
    }
}
