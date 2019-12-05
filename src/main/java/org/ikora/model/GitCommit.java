package org.ikora.model;

import java.util.Date;

public class GitCommit {
    final private String id;
    final private Date date;

    GitCommit(String id, Date date){
        this.id = id;
        this.date = date;
    }

    public Date getDate(){
        return this.date;
    }

    public String getId(){
        return this.id;
    }
}
