package lu.uni.serval.robotframework.model;

import java.io.File;
import java.util.*;

public class KeywordTable<T extends KeywordDefinition> implements Iterable<T> {
    private HashMap<String, T> keywords;
    private String file;

    public KeywordTable() {
        this.keywords = new HashMap<>();
    }

    public void setFile(String file) {
        this.file = file;

        for (T userKeyword: keywords.values()){
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

    public T findKeyword(T keyword){
        return keywords.getOrDefault(getKey(keyword), null);
    }

    public T findKeyword(String name){
        return findKeyword(null, name);
    }

    public T findKeyword(String file, String name){
        for(T keyword: keywords.values()){
            if(matches(file, name, keyword)){
                return keyword;
            }
        }

        return null;
    }

    private boolean matches(String file, String name, T keyword){
        if(file == null){
            return keyword.matches(name);
        }

        return file.equalsIgnoreCase(keyword.getFile()) && keyword.matches(name);
    }

    public int size() {
        return this.keywords.size();
    }

    public boolean isEmpty() {
        return this.keywords.isEmpty();
    }

    public boolean contains(T keyword) {
        if(keyword == null){
            return false;
        }

        return this.keywords.containsKey(getKey(keyword));
    }

    @Override
    public Iterator<T> iterator() {
        return this.keywords.values().iterator();
    }

    public List<T> asList() {
        return new ArrayList<>(keywords.values());
    }

    public boolean add(T keyword) {
        if(keyword == null){
            return false;
        }

        keywords.put(getKey(keyword), keyword);

        return true;
    }

    public boolean remove(T keyword) {
        if(keyword == null){
            return false;
        }

        this.keywords.remove(getKey(keyword));

        return true;
    }

    public boolean extend(KeywordTable<T> table) {
        for(T keyword: table.keywords.values()){
            this.keywords.put(getKey(keyword), keyword);
        }

        return true;
    }

    public void clear() {
        this.keywords.clear();
    }

    private String getKey(T keyword){
        return keyword.getFile() + File.separator + keyword.getName();
    }
}
