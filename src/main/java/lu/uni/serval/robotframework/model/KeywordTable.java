package lu.uni.serval.robotframework.model;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;

public class KeywordTable implements Iterable<UserKeyword> {
    private HashMap<String, UserKeyword> keywords;
    private String file;

    public KeywordTable(){
        keywords = new HashMap<>();
    }

    public void setFile(String file) {
        this.file = file;

        for (UserKeyword userKeyword: keywords.values()){
            userKeyword.setFile(this.file);
        }
    }

    public String getFile() {
        return file;
    }

    public KeywordTable(KeywordTable other){
        keywords = other.keywords;

        file = other.file;
    }

    public UserKeyword findKeyword(UserKeyword keyword){
        return keywords.getOrDefault(getKey(keyword), null);
    }

    public UserKeyword findKeyword(String name){
        return findKeyword(null, name);
    }

    public UserKeyword findKeyword(String file, String name){
        for(UserKeyword userKeyword: keywords.values()){
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

    public int size() {
        return this.keywords.size();
    }

    public boolean isEmpty() {
        return this.keywords.isEmpty();
    }

    public boolean contains(UserKeyword keyword) {
        if(keyword == null){
            return false;
        }

        return this.keywords.containsKey(getKey(keyword));
    }

    @Override
    public Iterator<UserKeyword> iterator() {
        return this.keywords.values().iterator();
    }

    public Object[] toArray() {
        return this.keywords.values().toArray();
    }

    public <T> T[] toArray(T[] a) {
        return this.keywords.values().toArray(a);
    }

    public boolean add(UserKeyword userKeyword) {
        if(userKeyword == null){
            return false;
        }

        keywords.put(getKey(userKeyword), userKeyword);

        return true;
    }

    public boolean remove(UserKeyword keyword) {
        if(keyword == null){
            return false;
        }

        this.keywords.remove(getKey(keyword));

        return true;
    }

    public boolean extend(KeywordTable table) {
        for(UserKeyword keyword: table.keywords.values()){
            this.keywords.put(getKey(keyword), keyword);
        }

        return true;
    }

    public void clear() {
        this.keywords.clear();
    }

    private String getKey(UserKeyword keyword){
        return keyword.getFile() + File.separator + keyword.getName();
    }
}
