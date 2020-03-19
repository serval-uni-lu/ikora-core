package tech.ikora.model;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Optional;
import java.util.Set;

public class Projects implements Iterable<Project> {
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

    public <T extends Node> Set<T> getNodes(Class<T> type){
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
}
