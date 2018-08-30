package lu.uni.serval.analytics;

import lu.uni.serval.robotframework.model.Project;

public class ProjectStatistics {
    final private Project project;

    ProjectStatistics(Project project){
        this.project = project;
    }

    public int getNumberFiles(){
        return this.project.getTestCaseFiles().size();
    }

    public int getNumberKeywords(){
        return this.project.getUserKeywords().size();
    }
}
