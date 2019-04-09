package org.ukwikora.model;

import javax.annotation.Nonnull;
import java.io.File;
import java.util.*;

public class StatementTable<T extends Statement> implements Iterable<T> {
    private HashMap<String, T> statementMap;
    private Set<T> statementSet;
    private TestCaseFile file;

    public StatementTable() {
        this.statementMap = new HashMap<>();
        this.statementSet = new HashSet<>();
    }

    public void setFile(TestCaseFile file) {
        this.file = file;

        statementMap = new HashMap<>();
        for (T statement: statementSet){
            statement.setFile(this.file);
            statementMap.put(getKey(statement), statement);
        }
    }

    public TestCaseFile getFile() {
        return file;
    }

    public StatementTable(StatementTable<T> other){
        statementMap = other.statementMap;
        statementSet = other.statementSet;

        file = other.file;
    }

    public Set<T> findStatement(T statement){
        return findStatement(statement.getFileName(), statement.getName());
    }

    public Set<T> findStatement(String name){
        return findStatement(null, name);
    }

    public Set<T> findStatement(String file, String name){
        Set<T> statments = new HashSet<>();

        for(T statement: statementSet){
            if(matches(file, name, statement)){
                statments.add(statement);
            }
        }

        return statments;
    }

    private boolean matches(String file, String name, T statement){
        if(file == null){
            return statement.matches(name);
        }

        return file.equalsIgnoreCase(statement.getFileName()) && statement.matches(name);
    }

    public int size() {
        return this.statementSet.size();
    }

    public boolean isEmpty() {
        return this.statementSet.isEmpty();
    }

    public boolean contains(T statement) {
        if(statement == null){
            return false;
        }

        return this.statementSet.contains(statement);
    }

    @Override
    @Nonnull
    public Iterator<T> iterator() {
        return this.statementSet.iterator();
    }

    public List<T> asList() {
        return new ArrayList<>(statementSet);
    }

    public boolean add(T statement) {
        if(statement == null){
            return false;
        }

        if(statementSet.contains(statement)){
            return false;
        }

        statementSet.add(statement);
        statementMap.put(getKey(statement), statement);

        return true;
    }

    public boolean remove(T statement) {
        if(statement == null){
            return false;
        }

        this.statementMap.remove(getKey(statement));
        this.statementSet.remove(statement);

        return true;
    }

    public void extend(StatementTable<T> table) {
        for(T statement: table){
            if(statementSet.contains(statement)){
                continue;
            }

            this.statementSet.add(statement);
            this.statementMap.put(getKey(statement), statement);
        }
    }

    public Set<T> toSet(){
        return this.statementSet;
    }

    public void clear() {
        this.statementSet.clear();
        this.statementMap.clear();
    }

    private String getKey(T statement){
        return statement.getFile() + File.separator + statement.getName();
    }
}
