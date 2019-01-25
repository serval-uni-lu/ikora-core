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

    public ProjectsSummary(List<Project> projects){
        try {
            int size = projects.size();

            links = new ArrayList<>(size);
            labels = new ArrayList<>(size);
            lines = new ArrayList<>(size);

            for(Project project: projects){
                String name = project.getName();

                links.add(new Link(name));
                labels.add(StringUtils.toBeautifulName(name));
                lines.add(project.getLoc());
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
        try {
            return JsonUtils.convertToJsonArray(labels);
        } catch (IOException e) {
            logger.error(String.format("Failed to get json names project summary: %s", e.getMessage()));
            return "[]";
        }
    }

    public String getJsonLines(){
        try {
            return JsonUtils.convertToJsonArray(lines);
        } catch (IOException e) {
            logger.error(String.format("Failed to get json lines for project summary: %s", e.getMessage()));
            return "[]";
        }
    }

    class Link{
        String text;
        String url;

        Link(String name) throws UnsupportedEncodingException {
            text = StringUtils.lineTruncate(StringUtils.toBeautifulName(name), 20);
            url = StringUtils.toBeautifulUrl(name, "html");
        }
    }
}
