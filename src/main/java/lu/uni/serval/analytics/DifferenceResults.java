package lu.uni.serval.analytics;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lu.uni.serval.utils.ReportKeywordData;
import lu.uni.serval.utils.tree.EditAction;
import lu.uni.serval.utils.tree.EditOperation;
import lu.uni.serval.utils.tree.TreeNode;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.time.LocalDateTime;
import java.util.*;

@JsonSerialize(using = DifferenceResultSerializer.class)
public class DifferenceResults implements Map<Pair<LocalDateTime, LocalDateTime>, List<EditAction>>{
    private Map<Pair<LocalDateTime, LocalDateTime>, List<EditAction>> differences;

    DifferenceResults(){
        differences = new HashMap<>();
    }

    public void addDifference(EditAction difference) {
        LocalDateTime dateTime1 = difference.getNode1() == null ? null :((ReportKeywordData)difference.getNode1().data).executionDate;
        LocalDateTime dateTime2 = difference.getNode2() == null ? null : ((ReportKeywordData)difference.getNode2().data).executionDate;

        ImmutablePair<LocalDateTime, LocalDateTime> dates = new ImmutablePair<>(dateTime1, dateTime2);

        put(dates, difference);
    }

    @Override
    public int size() {
        return differences.size();
    }

    @Override
    public boolean isEmpty() {
        return differences.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return differences.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return differences.containsValue(value);
    }

    @Override
    public List<EditAction> get(Object key) {
        return differences.get(key);
    }

    @Override
    public List<EditAction> put(Pair<LocalDateTime, LocalDateTime> key, List<EditAction> value) {
        return differences.put(key, value);
    }

    public List<EditAction> put(Pair <LocalDateTime, LocalDateTime> key, EditAction value) {
        if(!this.differences.containsKey(key)){
            put(key, new ArrayList<>());
        }

        List<EditAction> differences = this.differences.get(key);

        for(int i = differences.size() - 1; i >= 0; --i){
            EditAction current = differences.get(i);

            if(current.operation != value.operation){
                continue;
            }

            TreeNode currentNode = current.operation != EditOperation.Delete ? current.getNode1() : current.getNode2();
            TreeNode valueNode = value.operation != EditOperation.Delete ? value.getNode1() : value.getNode2();

            if(currentNode == null || valueNode == null){
                continue;
            }

            if(currentNode.isAncestor(valueNode)){
                value = null;
                break;
            }

            if(valueNode.isAncestor(currentNode)){
                differences.remove(current);
            }
        }

        if(value != null){
            differences.add(value);
        }

        return differences;
    }

    @Override
    public List<EditAction> remove(Object key) {
        return differences.remove(key);
    }

    @Override
    public void putAll(Map<? extends Pair<LocalDateTime, LocalDateTime>, ? extends List<EditAction>> m) {
        differences.putAll(m);
    }

    @Override
    public void clear() {
        differences.clear();
    }

    @Override
    public Set<Pair<LocalDateTime, LocalDateTime>> keySet() {
        return differences.keySet();
    }

    @Override
    public Collection<List<EditAction>> values() {
        return differences.values();
    }

    @Override
    public Set<Entry<Pair<LocalDateTime, LocalDateTime>, List<EditAction>>> entrySet() {
        return differences.entrySet();
    }
}
