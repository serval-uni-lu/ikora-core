package lu.uni.serval.robotframework.model;

import java.util.*;

public class KeywordTable implements Set<UserKeyword> {
    private Set<UserKeyword> userKeywordSet;

    public KeywordTable(){
        userKeywordSet = new HashSet<>();
    }

    public KeywordTable(KeywordTable other){
        userKeywordSet = other.userKeywordSet;
    }

    public UserKeyword findKeyword(UserKeyword keyword){
        if(userKeywordSet.contains(keyword)){
            return keyword;
        }

        String file = keyword.getFile();
        String name = keyword.getName().toString();

        return findKeyword(file, name);
    }

    public UserKeyword findKeyword(String name){
        return findKeyword(null, name);
    }

    public UserKeyword findKeyword(String file, String name){
        for(UserKeyword userKeyword: userKeywordSet){
            if(matches(file, name, userKeyword)){
                return userKeyword;
            }
        }

        return null;
    }

    private boolean matches(String file, String name, UserKeyword userKeyword){
        if(file == null){
            return userKeyword.matches(name);
        }

        return file.equalsIgnoreCase(userKeyword.getFile()) && userKeyword.matches(name);
    }

    @Override
    public int size() {
        return this.userKeywordSet.size();
    }

    @Override
    public boolean isEmpty() {
        return this.userKeywordSet.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return this.userKeywordSet.contains(o);
    }

    @Override
    public Iterator<UserKeyword> iterator() {
        return this.userKeywordSet.iterator();
    }

    @Override
    public Object[] toArray() {
        return this.userKeywordSet.toArray();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return this.userKeywordSet.toArray(a);
    }

    public boolean add(UserKeyword userKeyword) {
        return this.userKeywordSet.add(userKeyword);
    }

    @Override
    public boolean remove(Object o) {
        return this.userKeywordSet.remove(o);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return this.userKeywordSet.containsAll(c);
    }

    @Override
    public boolean addAll(Collection<? extends UserKeyword> c) {
        return this.userKeywordSet.addAll(c);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return this.userKeywordSet.retainAll(c);
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return this.userKeywordSet.removeAll(c);
    }

    @Override
    public void clear() {
        this.userKeywordSet.clear();
    }
}
