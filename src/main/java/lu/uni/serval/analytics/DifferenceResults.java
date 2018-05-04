package lu.uni.serval.analytics;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lu.uni.serval.utils.ReportKeywordData;
import lu.uni.serval.utils.tree.EditAction;
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

    public void setDifferences(List<EditAction> differences) {
        for(EditAction difference: differences){
            LocalDateTime dateTime1 = difference.getNode1() == null ? null :((ReportKeywordData)difference.getNode1().data).executionDate;
            LocalDateTime dateTime2 = difference.getNode2() == null ? null : ((ReportKeywordData)difference.getNode2().data).executionDate;

            ImmutablePair<LocalDateTime, LocalDateTime> dates = new ImmutablePair<>(dateTime1, dateTime2);

            if(!this.differences.containsKey(dates)){
                this.differences.put(dates, new ArrayList<>());
            }

            this.differences.get(dates).add(difference);
        }
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
