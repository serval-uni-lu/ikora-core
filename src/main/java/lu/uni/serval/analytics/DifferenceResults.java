package lu.uni.serval.analytics;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lu.uni.serval.utils.EasyPair;
import lu.uni.serval.utils.ReportKeywordData;
import lu.uni.serval.utils.tree.EditAction;
import lu.uni.serval.utils.tree.EditOperation;
import lu.uni.serval.utils.tree.LabelTreeNode;
import org.apache.commons.lang3.tuple.Pair;

import java.time.LocalDateTime;
import java.util.*;

@JsonSerialize(using = DifferenceResultSerializer.class)
public class DifferenceResults implements Map<Pair<LocalDateTime, LocalDateTime>, List<EditAction>>{
    private Map<Pair<LocalDateTime, LocalDateTime>, List<EditAction>> differences;
    public OperationCounter counter;

    DifferenceResults(){
        differences = new LinkedHashMap<>();
        counter = new OperationCounter();
    }

    public void addDifference(EditAction difference) {
        LocalDateTime dateTime1 = difference.getRoot1() == null ? null :((ReportKeywordData)difference.getRoot1().getData()).executionDate;
        LocalDateTime dateTime2 = difference.getRoot2() == null ? null : ((ReportKeywordData)difference.getRoot2().getData()).executionDate;

        EasyPair<LocalDateTime, LocalDateTime> dates = new EasyPair<>(dateTime1, dateTime2);

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
        LabelTreeNode valueNode = value.operation != EditOperation.Delete ? value.getNode1() : value.getNode2();

        for(int i = differences.size() - 1; i >= 0; --i){
            EditAction current = differences.get(i);

            if(current.operation != value.operation){
                continue;
            }

            LabelTreeNode currentNode = current.operation != EditOperation.Delete ? current.getNode1() : current.getNode2();

            if(currentNode == null){
                continue;
            }

            if(currentNode.getData().isSame(valueNode.getData()) || currentNode.isAncestor(valueNode)){
                value = null;
                break;
            }

            if(valueNode.isAncestor(currentNode)){
                subtract(key, current.operation);
                differences.remove(current);
            }
        }

        if(value != null){
            add(key, value.operation);
            differences.add(value);
        }

        return differences;
    }

    private void subtract(Pair<LocalDateTime, LocalDateTime> key, EditOperation operation) {
        counter.substract(new EasyPair<>(null, null), operation);
        counter.substract(key, operation);
    }

    private void add(Pair<LocalDateTime, LocalDateTime> key, EditOperation operation) {
        counter.add(new EasyPair<>(null, null), operation);
        counter.add(key, operation);
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

    public class OperationCounter{
        private Map<Pair<LocalDateTime, LocalDateTime>, Map<EditOperation, Integer>> counter;

        public OperationCounter(){
            counter = new HashMap<>();
        }

        public void add(Pair<LocalDateTime, LocalDateTime> dates, EditOperation operation){
            if(!counter.containsKey(dates)){
                initializeCounter(dates);
            }

            int value = counter.get(dates).get(operation) + 1;
            counter.get(dates).put(operation, value);
        }

        private void initializeCounter(Pair<LocalDateTime, LocalDateTime> dates) {
            Map<EditOperation, Integer> operations = new HashMap<>();
            for(EditOperation op: EditOperation.values()){
                operations.put(op, 0);
            }

            counter.put(dates, operations);
        }

        public void substract(Pair<LocalDateTime, LocalDateTime> dates, EditOperation operation){
            if(!counter.containsKey(dates)){
                return;
            }

            int value = counter.get(dates).get(operation) - 1;
            counter.get(dates).put(operation, value);
        }

        public Map<EditOperation, Integer> getOperations(){
            Pair<LocalDateTime, LocalDateTime> dates = new EasyPair<>(null, null);
            if(!counter.containsKey(dates)){
                initializeCounter(dates);
            }

            return counter.get(dates);
        }

        public Map<EditOperation, Integer> getOperations(Pair<LocalDateTime, LocalDateTime> dates){
            if(!counter.containsKey(dates)){
                initializeCounter(dates);
            }

            return counter.get(dates);
        }
    }
}
