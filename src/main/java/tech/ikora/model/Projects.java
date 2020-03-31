package tech.ikora.model;

import java.util.*;

public class Projects implements Iterable<Project> {
    private Date date;
    private Set<Project> projectSet;

    public Projects(){
        this.projectSet = new HashSet<>();
    }

    public Projects(Project project){
        this.projectSet = new HashSet<>();
        this.projectSet.add(project);
    }

    public Projects(Set<Project> projects){
        this.projectSet = projects;
    }

    public Set<Project> asSet() {
        return this.projectSet;
    }

    public void addProject(Project project){
        if(project == null){
            return;
        }

        projectSet.add(project);
    }

    public void addProjects(Projects projects){
        for (Project project: projects.projectSet){
            addProject(project);
        }
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public boolean contains(Project project){
        return projectSet.contains(project);
    }

    public Optional<Project> findProjectByName(String name){
        for(Project project: projectSet){
            if(project.getName().equalsIgnoreCase(name)){
                return Optional.of(project);
            }
        }

        return Optional.empty();
    }

    public <T extends SourceNode> Set<T> getNodes(Class<T> type){
        Set<T> nodes = new HashSet<>();

        for(Project project: projectSet){
            nodes.addAll(project.getNodes(type));
        }

        return nodes;
    }

    @Override
    public Iterator<Project> iterator() {
        return projectSet.iterator();
    }

    public int size() {
        return projectSet.size();
    }

    public boolean isEmpty() {
        return projectSet.isEmpty();
    }
}
