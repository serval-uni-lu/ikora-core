package lu.uni.serval.analytics;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import lu.uni.serval.robotframework.report.KeywordStatus;
import lu.uni.serval.robotframework.report.Report;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.io.File;
import java.time.LocalDateTime;
import java.util.*;

@JsonSerialize(using = StatusResultSerializer.class)
public class StatusResults {
    public class KeywordInfo {
        private List<String> info;

        public KeywordInfo(KeywordStatus keyword){
            info = new ArrayList<>(3);

            File file = new File(keyword.getSource());

            info.add(keyword.getName());
            info.add(file.getName());
        }

        String getName(){
            return info.get(0);
        }

        String getFile(){
            return info.get(1);
        }

        String getLibrary(){
            return info.get(2);
        }

        @Override
        public int hashCode() {
            return info.hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            if(obj instanceof KeywordInfo){
                return info.equals(((KeywordInfo)obj).info);
            }

            return false;
        }
    }

    private Map<KeywordInfo, Map<Report, KeywordStatus>> keywords;
    private Map<KeywordStatus.Status, Map<Report, Integer>> total;
    private Map<Report, MutablePair<Integer, Integer>> failureRate;

    public StatusResults(){
        this.keywords = new HashMap<>();
        this.total = new HashMap<>();
        this.failureRate = new HashMap<>();

        this.total.put(KeywordStatus.Status.PASS, new HashMap<>());
        this.total.put(KeywordStatus.Status.FAIL, new HashMap<>());
    }

    public void add(KeywordStatus keyword){
        if(keyword == null){
            return;
        }

        if(keyword.getType() != KeywordStatus.Type.TESTCASE){
            return;
        }

        //updateKeywords(keyword);
        //updateTotal(keyword);
    }

    public Set<KeywordInfo> getKeywordsInfo() {
        return keywords.keySet();
    }

    public List<Pair<Report, Integer>> getTotal(KeywordStatus.Status status){
        Map<Report, Integer> map = total.get(status);

        SortedSet<Report> keys = new TreeSet<>(map.keySet());

        List<Pair<Report, Integer>> array = new ArrayList<>(keys.size());

        for(Report key: keys){
            array.add(ImmutablePair.of(key, map.get(key)));
        }

        return array;
    }

    public List<Pair<Report, KeywordStatus>> getKeyword(KeywordInfo info){
        Map<Report, KeywordStatus> map = keywords.get(info);

        SortedSet<Report> keys = new TreeSet<>(keywords.get(info).keySet());

        List<Pair<Report, KeywordStatus>> array = new ArrayList<>(keys.size());

        for(Report key: keys){
            array.add(ImmutablePair.of(key, map.get(key)));
        }

        return array;
    }

    public double getFailureRate(LocalDateTime executionDate) {
        MutablePair<Integer, Integer> pair = failureRate.getOrDefault(executionDate, MutablePair.of(0,0));

        if(pair.left == 0){
            return 0.0;
        }

        return (double)pair.left / pair.right;
    }

 /*
    private void updateTotal(KeywordStatus keyword) {
        // update total counter
        Map<LocalDateTime, Integer> entry = total.get(keyword.getStatus());
        int value = entry.getOrDefault(data.executionDate, 0) + 1;
        entry.put(data.executionDate, value);

        // update failure rate
        MutablePair<Integer, Integer> pair = failureRate.getOrDefault(data.executionDate, MutablePair.of(0, 0));
        pair.setLeft(pair.left + (keyword.getStatus() == KeywordStatus.Status.FAIL ? 1 : 0));
        pair.setRight(pair.right + 1);
        failureRate.put(data.executionDate, pair);
    }

    private void updateKeywords(KeywordStatus keyword) {
        KeywordInfo info = new KeywordInfo(keyword);

        if(!keywords.containsKey(info)){
            keywords.put(info, new HashMap<>());
        }

        keywords.get(info).put(data.executionDate, keyword);
    }

    public boolean isServiceDown(KeywordStatus keyword) {
        LocalDateTime executionDate = keyword.getReportExecutionDate();
        return this.getFailureRate(executionDate) > 0.5;
    }
 */
}
