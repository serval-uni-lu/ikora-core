package lu.uni.serval.analytics;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lu.uni.serval.utils.ReportKeywordData;
import lu.uni.serval.utils.tree.LabelTreeNode;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.io.File;
import java.time.LocalDateTime;
import java.util.*;

@JsonSerialize(using = StatusResultSerializer.class)
public class StatusResults {

    public class KeywordInfo {
        private List<String> info;
        private LabelTreeNode keyword;

        public KeywordInfo(LabelTreeNode keyword){
            ReportKeywordData data = StatusResults.getReportData(keyword);
            this.keyword = keyword;

            info = new ArrayList<>(3);

            File file = new File(data.file);

            info.add(data.name);
            info.add(file.getName());
            info.add(data.library);
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

        int getSize(){
            return keyword.getNodeCount();
        }

        int getNumberLeaves(){
            return keyword.getLeafCount();
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

    private Map<KeywordInfo, Map<LocalDateTime, ReportKeywordData.Status>> keywords;
    private Map<ReportKeywordData.Status, Map<LocalDateTime, Integer>> total;

    public StatusResults(){
        this.keywords = new HashMap<>();
        this.total = new HashMap<>();

        this.total.put(ReportKeywordData.Status.PASS, new HashMap<>());
        this.total.put(ReportKeywordData.Status.FAILED, new HashMap<>());
    }

    public void add(LabelTreeNode keyword){
        if(keyword == null){
            return;
        }



        if(!getReportData(keyword).type.equalsIgnoreCase("test")){
            return;
        }

        updateKeywords(keyword);
        updateTotal(keyword);
    }

    public Set<KeywordInfo> getKeywordsInfo() {
        return keywords.keySet();
    }

    public List<Pair<LocalDateTime, Integer>> getTotal(ReportKeywordData.Status status){
        Map<LocalDateTime, Integer> map = total.get(status);

        SortedSet<LocalDateTime> keys = new TreeSet<>(map.keySet());

        List<Pair<LocalDateTime, Integer>> array = new ArrayList<>(keys.size());

        for(LocalDateTime key: keys){
            array.add(ImmutablePair.of(key, map.get(key)));
        }

        return array;
    }

    public List<Pair<LocalDateTime, ReportKeywordData.Status>> getKeyword(KeywordInfo info){
        Map<LocalDateTime, ReportKeywordData.Status> map = keywords.get(info);

        SortedSet<LocalDateTime> keys = new TreeSet<>(keywords.get(info).keySet());

        List<Pair<LocalDateTime, ReportKeywordData.Status>> array = new ArrayList<>(keys.size());

        for(LocalDateTime key: keys){
            array.add(ImmutablePair.of(key, map.get(key)));
        }

        return array;
    }

    private void updateTotal(LabelTreeNode keyword) {
        if(!(keyword.getData() instanceof ReportKeywordData)){
            throw new IllegalArgumentException("Expected ReportKeywordData type");
        }

        ReportKeywordData data = (ReportKeywordData) keyword.getData();

        Map<LocalDateTime, Integer> entry = total.get(data.status);
        int value = entry.getOrDefault(data.executionDate, 0) + 1;
        entry.put(data.executionDate, value);
    }

    private void updateKeywords(LabelTreeNode keyword) {
        KeywordInfo info = new KeywordInfo(keyword);

        if(!keywords.containsKey(info)){
            keywords.put(info, new HashMap<>());
        }

        if(!(keyword.getData() instanceof ReportKeywordData)){
            throw new IllegalArgumentException("Expected ReportKeywordData type");
        }

        ReportKeywordData data = (ReportKeywordData) keyword.getData();

        keywords.get(info).put(data.executionDate, data.status);
    }

    public static ReportKeywordData getReportData(LabelTreeNode keyword){
        if(!(keyword.getData() instanceof ReportKeywordData)){
            throw new IllegalArgumentException("Expected ReportKeywordData type");
        }

        return (ReportKeywordData)keyword.getData();
    }
}
