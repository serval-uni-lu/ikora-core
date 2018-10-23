package org.ukwikora.model;

import java.time.LocalDateTime;

public class GitCommit {
    final private String id;
    final private LocalDateTime dateTime;

    GitCommit(String id, LocalDateTime dateTime){
        this.id = id;
        this.dateTime = dateTime;
    }

    public LocalDateTime getDateTime(){
        return this.dateTime;
    }

    public String getId(){
        return this.id;
    }
}
