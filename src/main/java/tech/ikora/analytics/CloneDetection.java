package tech.ikora.analytics;

import java.util.*;

import tech.ikora.model.*;

public class CloneDetection<T extends Node> {
    private static Set<Action.Type> ignoreForType1;
    private static Set<Action.Type> ignoreForType2;

    static {
        Set<Action.Type> typeI = new HashSet<>(4);
        typeI.add(Action.Type.CHANGE_NAME);
        typeI.add(Action.Type.CHANGE_DOCUMENTATION);
        typeI.add(Action.Type.REMOVE_DOCUMENTATION);
        typeI.add(Action.Type.ADD_DOCUMENTATION);

        ignoreForType1 = Collections.unmodifiableSet(typeI);

        Set<Action.Type> typeII = new HashSet<>(ignoreForType1);
        typeII.add(Action.Type.CHANGE_STEP_ARGUMENT);

        ignoreForType2 = Collections.unmodifiableSet(typeII);
    }

    private Set<Project> projects;
    private Clones<T> clones;

    private CloneDetection(Project project){
        this.projects = Collections.singleton(project);
        this.clones = new Clones<>();
    }

    private CloneDetection(Set<Project> projects){
        this.projects = projects;
        this.clones = new Clones<>();
    }

    public static <T extends Node> Clones<T> findClones(Set<Project> projects, Class<T> type){
        CloneDetection<T> detection = new CloneDetection<>(projects);
        return detection.run(type);
    }

    public static <T extends Node> Clones<T> findClones(Project project, Class<T> type){
        CloneDetection<T> detection = new CloneDetection<>(project);
        return detection.run(type);
    }

    public static  <T extends Node> Clone.Type getCloneType(T t1, T t2){
        Clone.Type clone = Clone.Type.NONE;

        if(isTooShort(t1, t2)){
            return clone;
        }

        Difference difference = Difference.of(t1, t2);

        if(isSameSize(t1, t2)){
            if(isCloneType1(difference)){
                clone = Clone.Type.TYPE_1;
            }
            else if(isCloneType2(difference)){
                clone = Clone.Type.TYPE_2;
            }
        }

        if(clone == Clone.Type.NONE){
            if(isCloneType3(difference)){
                clone = Clone.Type.TYPE_3;
            }
        }

        return clone;
    }

    private Clones<T> run(Class<T> type){
        List<T> nodes = new ArrayList<>();

        for(Project project: projects){
            nodes.addAll(project.getNodes(type));
        }

        int size = nodes.size();

        for(int i = 0; i < size; ++i){
            T t1 = nodes.get(i);

            for(int j = i + 1; j < size; ++j){
                T t2 = nodes.get(j);
                clones.update(t1, t2, getCloneType(t1, t2));
            }
        }

        return clones;
    }

    private static <T extends Node> boolean isTooShort(T t1, T t2){
        if(KeywordDefinition.class.isAssignableFrom(t1.getClass())){
            KeywordDefinition keyword1 = (KeywordDefinition)t1;
            KeywordDefinition keyword2 = (KeywordDefinition)t2;

            return keyword1.getSteps().size() <= 1 || keyword2.getSteps().size() <= 1;
        }

        return false;
    }

    private static <T extends Node> boolean isSameSize(T t1, T t2){
        if(!KeywordDefinition.class.isAssignableFrom(t1.getClass())){
            return true;
        }

        return ((KeywordDefinition)t1).getSteps().size() == ((KeywordDefinition)t2).getSteps().size();
    }

    public static boolean isCloneType1(Difference difference){
        return difference.isEmpty(ignoreForType1);
    }

    public static boolean isCloneType2(Difference difference){
        return difference.isEmpty(ignoreForType2);
    }

    public static boolean isCloneType3(Difference difference){
        for(Action action: difference.getActions()){
            if (!ignoreForType2.contains(action.getType())
            && !canIgnoreForType3(action)){
                return false;
            }
        }

        return true;
    }

    private static boolean canIgnoreForType3(Action action) {

        switch (action.getType()){
            case CHANGE_STEP_ARGUMENT:
            case CHANGE_STEP: return isLogStep(action.getLeft()) && isLogStep(action.getRight());
            case ADD_STEP:
            case REMOVE_STEP: return isLogStep(action.getValue());
            default: return false;
        }
    }

    private static boolean isLogStep(Differentiable differentiable){
        if(differentiable == null){
            return false;
        }

        if(!KeywordCall.class.isAssignableFrom(differentiable.getClass())){
            return false;
        }

        Optional<Keyword> optionalKeyword = ((KeywordCall)differentiable).getKeyword();

        return optionalKeyword.filter(keyword -> keyword.getType() == Keyword.Type.LOG).isPresent();
    }
}
