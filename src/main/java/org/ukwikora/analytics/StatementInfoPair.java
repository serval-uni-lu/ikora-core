package org.ukwikora.analytics;

import org.ukwikora.model.Project;

public class StatementInfoPair<T> {
    private StatementInfo<T> left;
    private StatementInfo<T> right;

    static public <T> StatementInfoPair<T> of(Project project1, Project project2, T element1, T element2){
        StatementInfoPair<T> pair = new StatementInfoPair<>();

        pair.left = new StatementInfo<>(project1, element1);
        pair.right = new StatementInfo<>(project2, element2);

        return pair;
    }

    public StatementInfo<T> getLeft() {
        return left;
    }

    public StatementInfo<T> getRight() {
        return right;
    }

    public T getElement(Project project){
        if(project == left.getProject()){
            return left.getStatement();
        }
        else if(project == right.getProject()){
            return right.getStatement();
        }

        return null;
    }
}
