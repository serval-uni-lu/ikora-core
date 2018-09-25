package lu.uni.serval.robotframework.model;

import java.io.File;
import java.util.*;

public class ElementTable<T extends Element> implements Iterable<T> {
    private HashMap<String, T> elementMap;
    private Set<T> elementSet;
    private TestCaseFile file;

    public ElementTable() {
        this.elementMap = new HashMap<>();
        this.elementSet = new HashSet<>();
    }

    public void setFile(TestCaseFile file) {
        this.file = file;

        elementMap = new HashMap<>();
        for (T element: elementSet){
            element.setFile(this.file);
            elementMap.put(getKey(element), element);
        }
    }

    public TestCaseFile getFile() {
        return file;
    }

    public ElementTable(ElementTable<T> other){
        elementMap = other.elementMap;
        elementSet = other.elementSet;

        file = other.file;
    }

    public T findElement(T element){
        return findElement(element.getFileName(), element.getName().toString());
    }

    public T findElement(String name){
        return findElement(null, name);
    }

    public T findElement(String file, String name){
        for(T keyword: elementSet){
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

        return file.equalsIgnoreCase(element.getFileName()) && element.matches(name);
    }

    public int size() {
        return this.elementSet.size();
    }

    public boolean isEmpty() {
        return this.elementSet.isEmpty();
    }

    public boolean contains(T element) {
        if(element == null){
            return false;
        }

        return this.elementSet.contains(element);
    }

    @Override
    public Iterator<T> iterator() {
        return this.elementSet.iterator();
    }

    public List<T> asList() {
        return new ArrayList<>(elementSet);
    }

    public boolean add(T element) {
        if(element == null){
            return false;
        }

        if(elementSet.contains(element)){
            return false;
        }

        elementSet.add(element);
        elementMap.put(getKey(element), element);

        return true;
    }

    public boolean remove(T element) {
        if(element == null){
            return false;
        }

        this.elementMap.remove(getKey(element));
        this.elementSet.remove(element);

        return true;
    }

    public void extend(ElementTable<T> table) {
        for(T element: table){
            if(elementSet.contains(element)){
                continue;
            }

            this.elementSet.add(element);
            this.elementMap.put(getKey(element), element);
        }
    }

    public void clear() {
        this.elementSet.clear();
        this.elementMap.clear();
    }

    private String getKey(T element){
        return element.getFile() + File.separator + element.getName();
    }
}
