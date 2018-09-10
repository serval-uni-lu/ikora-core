package lu.uni.serval.robotframework.model;

import java.io.File;
import java.util.*;

public class ElementTable<T extends Element> implements Iterable<T> {
    private HashMap<String, T> elements;
    private String file;

    public ElementTable() {
        this.elements = new HashMap<>();
    }

    public void setFile(String file) {
        this.file = file;

        HashMap<String, T> tmp = new HashMap<>();
        for (T element: elements.values()){
            element.setFile(this.file);
            tmp.put(getKey(element), element);
        }

        elements = tmp;
    }

    public String getFile() {
        return file;
    }

    public ElementTable(ElementTable other){
        elements = other.elements;

        file = other.file;
    }

    public T findElement(T keyword){
        return elements.getOrDefault(getKey(keyword), null);
    }

    public T findElement(String name){
        return findElement(null, name);
    }

    public T findElement(String file, String name){
        for(T keyword: elements.values()){
            if(matches(file, name, keyword)){
                return keyword;
            }
        }

        return null;
    }

    private boolean matches(String file, String name, T element){
        if(file == null){
            return element.matches(name);
        }

        return file.equalsIgnoreCase(element.getFile()) && element.matches(name);
    }

    public int size() {
        return this.elements.size();
    }

    public boolean isEmpty() {
        return this.elements.isEmpty();
    }

    public boolean contains(T keyword) {
        if(keyword == null){
            return false;
        }

        return this.elements.containsKey(getKey(keyword));
    }

    @Override
    public Iterator<T> iterator() {
        return this.elements.values().iterator();
    }

    public List<T> asList() {
        return new ArrayList<>(elements.values());
    }

    public boolean add(T element) {
        if(element == null){
            return false;
        }

        elements.put(getKey(element), element);

        return true;
    }

    public boolean remove(T keyword) {
        if(keyword == null){
            return false;
        }

        this.elements.remove(getKey(keyword));

        return true;
    }

    public boolean extend(ElementTable<T> table) {
        for(T keyword: table.elements.values()){
            this.elements.put(getKey(keyword), keyword);
        }

        return true;
    }

    public void clear() {
        this.elements.clear();
    }

    private String getKey(T element){
        return element.getFile() + File.separator + element.getName();
    }
}
