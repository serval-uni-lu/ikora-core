package lu.uni.serval.robotframework.model;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class KeywordTable implements Iterable<UserKeyword> {
    private final List<UserKeyword> userKeywordList = new ArrayList<UserKeyword>();

    @Nonnull
    public Iterator<UserKeyword> iterator() {
        return userKeywordList.iterator();
    }

    public void add(UserKeyword userKeyword) {
        userKeywordList.add(userKeyword);
    }
}
