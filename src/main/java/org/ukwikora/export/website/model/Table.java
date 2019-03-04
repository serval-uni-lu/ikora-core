package org.ukwikora.export.website.model;

import org.ukwikora.export.exception.InvalidNumberColumnException;

import java.util.ArrayList;
import java.util.List;

public class Table {
    private final String id;
    private final String name;
    private final String[] labels;

    private List<String[]> rows;

    public Table(String id, String name, String[] labels){
        this.id = id;
        this.name = name;
        this.labels = labels;
        this.rows = new ArrayList<>();
    }

    public void addRow(String[] row) throws InvalidNumberColumnException {
        if(row.length != labels.length){
            throw new InvalidNumberColumnException(String.format("Table expected %d, got %d instead", labels.length, row.length));
        }

        rows.add(row);
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String[] getLabels() {
        return labels;
    }

    public List<String[]> getRows() {
        return rows;
    }
}
