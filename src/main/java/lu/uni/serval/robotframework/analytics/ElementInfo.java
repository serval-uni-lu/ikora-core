package lu.uni.serval.robotframework.analytics;

import lu.uni.serval.robotframework.model.Project;

public class ElementInfo<T> {
    final private Project project;
    final private T element;

    ElementInfo(Project project, T element){
        this.project = project;
        this.element = element;
    }

    public T getElement() {
        return element;
    }

    public Project getProject() {
        return project;
    }
}
