package lu.uni.serval.analytics;

import lu.uni.serval.utils.tree.EditAction;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class DifferenceMemory {
    Map<LocalDateTime, Set<EditAction>> actions;

    DifferenceMemory(){
        actions = new HashMap<>();
    }

    public boolean addDifference(LocalDateTime date, EditAction difference){
        Set<EditAction> differences = this.actions.getOrDefault(date, new HashSet<>());

        if(differences.contains(difference)){
            return false;
        }

        differences.add(difference);
        this.actions.put(date, differences);

        return true;
    }
}
