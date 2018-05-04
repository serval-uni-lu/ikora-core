package lu.uni.serval.analytics;

import lu.uni.serval.utils.KeywordData;
import lu.uni.serval.utils.tree.TreeNode;
import org.apache.commons.lang3.tuple.ImmutablePair;

import java.util.*;

public class KeywordSequence implements Iterable<List<TreeNode>>{
    private Map<ReportKeywordId, List<TreeNode>> data;

    public KeywordSequence(){
        data = new HashMap<>();
    }

    public void add(TreeNode keyword){
        String file = ((KeywordData)keyword.data).file;
        String name = ((KeywordData)keyword.data).name;
        String library = ((KeywordData)keyword.data).library;

        ReportKeywordId key = new ReportKeywordId(file, name, library);

        if(data.containsKey(key)){
            data.get(key).add(keyword);
        }
        else{
            List<TreeNode> keywords = new ArrayList<>();
            keywords.add(keyword);

            data.put(key, keywords);
        }
    }

    @Override
    public Iterator<List<TreeNode>> iterator() {
        return data.values().iterator();
    }

    public class ReportKeywordId{
        public String file;
        public String library;
        public String name;

        public ReportKeywordId(String file, String library, String name){
            this.file = file;
            this.library = library;
            this.name = name;
        }
    }
}
