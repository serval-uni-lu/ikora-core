package org.ukwikora.model;

import javax.annotation.Nonnull;
import java.io.File;
import java.util.*;

public class StatementTable<T extends Statement> implements Iterable<T> {
    private HashMap<String, T> statementMap;
    private List<T> statementList;
    private TestCaseFile file;

    public StatementTable() {
        this.statementMap = new HashMap<>();
        this.statementList = new ArrayList<>();
    }

    public void setFile(TestCaseFile file) {
        this.file = file;

        statementMap = new HashMap<>();
        for (T statement: statementList){
            statement.setFile(this.file);
            statementMap.put(getKey(statement), statement);
        }
    }

    public TestCaseFile getFile() {
        return file;
    }

    public StatementTable(StatementTable<T> other){
        statementMap = other.statementMap;
        statementList = other.statementList;

        file = other.file;
    }

    public Set<T> findStatement(T statement){
        return findStatement(statement.getFileName(), statement.getName());
    }

    public Set<T> findStatement(String name){
        return findStatement(null, name);
    }

    public Set<T> findStatement(String file, String name){
        Set<T> statements = new HashSet<>();

        for(T statement: statementList){
            if(matches(file, name, statement)){
                statements.add(statement);
            }
        }

        return statements;
    }

    private boolean matches(String file, String name, T statement){
        if(file == null){
            return statement.matches(name);
        }

        return file.equalsIgnoreCase(statement.getFileName()) && statement.matches(name);
    }

    public int size() {
        return this.statementList.size();
    }

    public boolean isEmpty() {
        return this.statementList.isEmpty();
    }

    public boolean contains(T statement) {
        if(statement == null){
            return false;
        }

        return this.statementList.contains(statement);
    }

    @Override
    @Nonnull
    public Iterator<T> iterator() {
        return this.statementList.iterator();
    }

    public List<T> asList() {
        return statementList;
    }

    public boolean add(T statement) {
        if(statement == null){
            return false;
        }

        if(statementList.contains(statement)){
            return false;
        }

        statementList.add(statement);
        statementMap.put(getKey(statement), statement);

        return true;
    }

    public boolean remove(T statement) {
        if(statement == null){
            return false;
        }

        this.statementMap.remove(getKey(statement));
        this.statementList.remove(statement);

        return true;
    }

    public void extend(StatementTable<T> table) {
        for(T statement: table){
            if(statementList.contains(statement)){
                continue;
            }

            this.statementList.add(statement);
            this.statementMap.put(getKey(statement), statement);
        }
    }

    public void clear() {
        this.statementList.clear();
        this.statementMap.clear();
    }

    private String getKey(T statement){
        return statement.getFile() + File.separator + statement.getName();
    }
}
