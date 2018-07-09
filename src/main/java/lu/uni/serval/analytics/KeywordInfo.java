package lu.uni.serval.analytics;

import lu.uni.serval.robotframework.model.KeywordDefinition;
import lu.uni.serval.robotframework.model.Project;

public class KeywordInfo {
    final Project project;
    final KeywordDefinition keyword;

    KeywordInfo(Project project, KeywordDefinition keyword){
        this.project = project;
        this.keyword = keyword;
    }

    public KeywordDefinition getKeyword() {
        return keyword;
    }

    public Project getProject() {
        return project;
    }
}
