package lu.uni.serval.analytics;

import lu.uni.serval.robotframework.model.Project;

public class ElementInfoPair<T> {
    private ElementInfo<T> left;
    private ElementInfo<T> right;

    static public <T> ElementInfoPair<T> of(Project project1, Project project2, T element1, T element2){
        ElementInfoPair<T> pair = new ElementInfoPair<>();

        pair.left = new ElementInfo<>(project1, element1);
        pair.right = new ElementInfo<>(project2, element2);

        return pair;
    }


    public ElementInfo<T> getLeft() {
        return left;
    }

    public ElementInfo<T> getRight() {
        return right;
    }

    public T getElement(Project project){
        if(project == left.getProject()){
            return left.getElement();
        }
        else if(project == right.getProject()){
            return right.getElement();
        }

        return null;
    }
}
