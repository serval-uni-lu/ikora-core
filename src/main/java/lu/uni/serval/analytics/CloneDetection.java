package lu.uni.serval.analytics;

import lu.uni.serval.robotframework.model.*;

import java.util.*;

public class CloneDetection<T extends  Element> {
    private Project project;

    private CloneResults<T> clones;

    private CloneDetection(Project project){
        this.project = project;
        this.clones = new CloneResults<>();
    }

    public static <T extends  Element> CloneResults<T> findClones(Project project, Class<T> type){
        CloneDetection<T> detection = new CloneDetection<>(project);
        return detection.run(type);
    }

    private CloneResults<T> run(Class<T> type){
        List<T> elements = project.getElements(type).asList();
        int size = elements.size();

        for(int i = 0; i < size; ++i){
            T t1 = elements.get(i);

            for(int j = i + 1; j < size; ++j){
                T t2 = elements.get(j);
                clones.update(t1, t2, getCloneType(t1, t2));
            }
        }

        return clones;
    }

    private CloneResults.Type getCloneType(T t1, T t2){
        Difference difference = Difference.of(t1, t2);

        CloneResults.Type type = CloneResults.Type.None;

        if(isType1(t1, t2, difference)){
            type = CloneResults.Type.TypeI;
        }
        else if(isType2(t1, t2, difference)){
            type = CloneResults.Type.TypeII;
        }

        return type;
    }

    private boolean isType1(T t1, T t2, Difference difference) {
        if(isTooShort(t1, t2)){
            return false;
        }

        return difference.isEmpty(getIgnore(t1.getClass(), CloneResults.Type.TypeI));
    }

    private boolean isType2(T t1, T t2, Difference difference) {
        if(isTooShort(t1, t2)){
            return false;
        }

        return difference.isEmpty(getIgnore(t1.getClass(), CloneResults.Type.TypeII));
    }

    private boolean isTooShort(T t1, T t2){
        if(KeywordDefinition.class.isAssignableFrom(t1.getClass())){
            KeywordDefinition keyword1 = (KeywordDefinition)t1;
            KeywordDefinition keyword2 = (KeywordDefinition)t2;

            return keyword1.getSteps().size() <= 1 || keyword2.getSteps().size() <= 1;
        }

        return false;
    }

    private Action.Type[] getIgnore(Class<? extends Element> elementType, CloneResults.Type cloneType) {
        switch (cloneType){
            case TypeI:
            {
                if(KeywordDefinition.class.isAssignableFrom(elementType)){
                    return new Action.Type[]{Action.Type.CHANGE_NAME,
                            Action.Type.CHANGE_DOCUMENTATION,
                            Action.Type.REMOVE_DOCUMENTATION,
                            Action.Type.ADD_DOCUMENTATION};
                }
                else if (Variable.class.isAssignableFrom(elementType)){
                    return new Action.Type[]{Action.Type.CHANGE_NAME};
                }
            }
            break;

            case TypeII:
            {
                if(KeywordDefinition.class.isAssignableFrom(elementType)){
                    return new Action.Type[]{Action.Type.CHANGE_NAME,
                            Action.Type.CHANGE_DOCUMENTATION,
                            Action.Type.REMOVE_DOCUMENTATION,
                            Action.Type.ADD_DOCUMENTATION,
                            Action.Type.CHANGE_STEP_ARGUMENTS,
                            Action.Type.CHANGE_STEP_RETURN_VALUES};
                }
                else if (Variable.class.isAssignableFrom(elementType)){
                    return new Action.Type[]{Action.Type.CHANGE_NAME};
                }
            }
            break;

            case TypeIII:
                break;
            case TypeIV:
                break;
        }

        return new Action.Type[]{};
    }
}
