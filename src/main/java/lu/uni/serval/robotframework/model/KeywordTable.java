package lu.uni.serval.robotframework.model;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class KeywordTable implements Iterable<UserKeyword> {
    private List<UserKeyword> userKeywordList;

    public KeywordTable(){
        userKeywordList = new ArrayList<UserKeyword>();
    }

    public List<UserKeyword> getUserKeywords(){
        return userKeywordList;
    }

    @Nonnull
    public Iterator<UserKeyword> iterator() {
        return userKeywordList.iterator();
    }

    public void add(UserKeyword userKeyword) {
        userKeywordList.add(userKeyword);
    }
}
